package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.OrderRowService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
@Getter
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final ApplianceService applianceService;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final OrderService orderService;
    private final EmployeeRepository employeeRepository;
    private final OrderRowService orderRowService;

    @GetMapping
    @Loggable
    public String viewCart(HttpSession session, Model model) {
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new OrderDTO();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "cart/view";
    }
    @PostMapping("/add")
    @Loggable
    public String addToCart(@RequestParam Long applianceId, @RequestParam Long quantity, HttpSession session) {
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new OrderDTO();
            session.setAttribute("cart", cart);
        }
        ApplianceDTO appliance = applianceService.findById(applianceId)
                .orElseThrow(() -> new NoSuchElementException("Appliance not found"));
        OrderRowDTO existingRow = cart.getOrderRows().stream()
                .filter(row -> row.getAppliance().getId().equals(applianceId))
                .findFirst()
                .orElse(null);
        if (existingRow != null) {
            existingRow.setNumber(existingRow.getNumber() + quantity);
            existingRow.setAmount(appliance.getPrice().multiply(BigDecimal.valueOf(existingRow.getNumber())));
        } else {
            OrderRowDTO newRow = OrderRowDTO.builder()
                    .applianceName(appliance.getName())
                    .appliance(appliance)
                    .number(quantity)
                    .amount(appliance.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .editMode(false)
                    .build();
            cart.getOrderRows().add(newRow);
        }
        logger.info("Added to cart: Appliance ID={}, Quantity={}", applianceId, quantity);
        return "redirect:/cart";
    }
    @PostMapping("/update")
    @Loggable
    public String updateCartItem(@RequestParam String applianceName, @RequestParam Long quantity, HttpSession session) {
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart != null) {
            OrderRowDTO row = cart.getOrderRows().stream()
                    .filter(r -> r.getApplianceName().equals(applianceName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Item not found in cart"));
            row.setNumber(quantity);
            row.setAmount(row.getAppliance().getPrice().multiply(BigDecimal.valueOf(quantity)));
            logger.info("Updated cart item: Appliance Name={}, New Quantity={}", applianceName, quantity);
        }
        return "redirect:/cart";
    }
    @PostMapping("/remove")
    @Loggable
    public String removeCartItem(@RequestParam String applianceName, HttpSession session) {
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart != null) {
            cart.getOrderRows().removeIf(row -> row.getApplianceName().equals(applianceName));
            logger.info("Removed from cart: Appliance Name={}", applianceName);
        }
        return "redirect:/cart";
    }
    @GetMapping("/checkout")
    @Loggable
    public String showCheckoutPage(HttpSession session, Model model) {
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new OrderDTO();
            session.setAttribute("cart", cart);
        }
        BigDecimal totalAmount = cart.getOrderRows().stream()
                .map(OrderRowDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setAmount(totalAmount);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Client> optionalClient = clientRepository.findByEmail(username);
        ClientDTO client = optionalClient.map(clientMapper::toDTO).orElse(null);
        model.addAttribute("cart", cart);
        model.addAttribute("client", client);
        return "cart/checkout";
    }
    @PostMapping("/submitCheckout")
    @Loggable
    public String processCheckout(@RequestParam("cardNumber") String cardNumber, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isValidCardNumber(cardNumber)) {
            redirectAttributes.addFlashAttribute("error", "Invalid card number! Please use format: XXXX-XXXX");
            logger.warn("Invalid card number submitted: {}", cardNumber);
            return "redirect:/cart/checkout";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Client> optionalClient = clientRepository.findByEmail(username);
        Client clientEntity = optionalClient.orElse(null);
        if (clientEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Client not found.");
            logger.error("Client not found for username: {}", username);
            return "redirect:/cart/checkout";
        }
        if (clientEntity.getCard() == null || !clientEntity.getCard().equals(cardNumber)) {
            clientEntity.setCard(cardNumber);
            clientRepository.save(clientEntity);
            logger.info("Updated card number for client ID={}", clientEntity.getId());
        }
        OrderDTO cart = (OrderDTO) session.getAttribute("cart");
        if (cart == null || cart.getOrderRows().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            logger.warn("Attempted checkout with empty cart for client ID={}", clientEntity.getId());
            return "redirect:/cart/checkout";
        }
        cart.setAmount(cart.getOrderRows().stream()
                .map(OrderRowDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        List<com.epam.rd.autocode.assessment.appliances.model.Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No employees available to assign the order.");
            logger.error("No employees available to assign the order for client ID={}", clientEntity.getId());
            return "redirect:/cart/checkout";
        }
        Random random = new Random();
        com.epam.rd.autocode.assessment.appliances.model.Employee randomEmployee = employees.get(random.nextInt(employees.size()));
        logger.info("Assigning order to employee ID={}, Name={}", randomEmployee.getId(), randomEmployee.getName());
        OrderDTO orderDto = OrderDTO.builder()
                .clientId(clientEntity.getId())
                .clientName(clientEntity.getName())
                .employeeId(randomEmployee.getId())
                .employeeName(randomEmployee.getName())
                .approved(false)
                .orderRows(cart.getOrderRows())
                .amount(cart.getAmount())
                .build();
        logger.debug("Creating order: {}", orderDto);
        OrderDTO createdOrder = orderService.createOrder(orderDto);
        logger.info("Order created successfully for client ID={}", clientEntity.getId());
        for (OrderRowDTO row : cart.getOrderRows()) {
            orderRowService.addOrderRow(createdOrder.getId(), row.getApplianceName(), row.getNumber());
            logger.info("Added order row: Appliance Name={}, Quantity={}", row.getApplianceName(), row.getNumber());
        }
        session.removeAttribute("cart");
        logger.info("Cleared cart for client ID={}", clientEntity.getId());
        redirectAttributes.addFlashAttribute("success", "Order successfully submitted and assigned to an employee!");
        return "redirect:/cart/order-submitted";
    }
    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{4}-\\d{4}");
    }
    @GetMapping("/order-submitted")
    @Loggable
    public String orderSubmitted() {
        return "cart/order-submitted";
    }
}