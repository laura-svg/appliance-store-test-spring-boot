package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderRowService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final OrderRowService orderRowService;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final ApplianceRepository applianceRepository;

    @GetMapping
    @Loggable
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Model model) {
        List<OrderDTO> orders = orderService.findOrdersWithPagination(page, size, sort, order);
        int totalOrders = orderService.findAll().size();
        int totalPages = (int) Math.ceil((double) totalOrders / size);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "order/orders";
    }
    @GetMapping("/new")
    @Loggable
    public String newOrderForm(Model model) {
        model.addAttribute("orderDto", new OrderDTO());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "order/newOrder";
    }
    @PostMapping
    @Loggable
    public String createOrder(@ModelAttribute("orderDto") OrderDTO orderDto) {
        orderService.createOrder(orderDto);
        return "redirect:/orders";
    }
    @GetMapping("/{id}/edit")
    @Loggable
    public String editOrderForm(@PathVariable Long id, Model model) {
        OrderDTO order = orderService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        model.addAttribute("orderDto", order);
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "order/editOrder";
    }
    @PostMapping("/{id}/edit")
    @Loggable
    public String updateOrder(@PathVariable Long id, @ModelAttribute("orderDto") OrderDTO orderDto) {
        orderService.updateOrder(id, orderDto);
        return "redirect:/orders";
    }
    @PostMapping("/{id}/delete")
    @Loggable
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
    @PostMapping("/{id}/approve")
    @Loggable
    public String approveOrder(@PathVariable Long id) {
        orderService.approveOrder(id);
        return "redirect:/orders";
    }
    @PostMapping("/{id}/disapprove")
    @Loggable
    public String disapproveOrder(@PathVariable Long id) {
        orderService.disapproveOrder(id);
        return "redirect:/orders";
    }
    @GetMapping("/{id}/choiceAppliance")
    @Loggable
    public String viewOrderRows(@PathVariable Long id,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(defaultValue = "applianceName") String sort,
                                @RequestParam(defaultValue = "asc") String orderSort,
                                Model model) {
        OrderDTO order = orderService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        List<OrderRowDTO> rows = orderRowService.findAllByOrderId(id);
        int totalRows = rows.size();
        int totalPages = (int) Math.ceil((double) totalRows / size);
        model.addAttribute("order", order);
        model.addAttribute("rows", rows);
        model.addAttribute("appliances", applianceRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("orderSort", orderSort);
        return "order/choiceAppliance";
    }
    @PostMapping("/{id}/rows/add")
    @Loggable
    public String addOrderRow(@PathVariable Long id,
                              @RequestParam String applianceName,
                              @RequestParam Long quantity) {
        orderRowService.addOrderRow(id, applianceName, quantity);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
    @PostMapping("/{id}/rows/remove")
    @Loggable
    public String removeOrderRow(@PathVariable Long id, @RequestParam(required = false) Long rowId) {
        if (rowId == null) {
            throw new IllegalArgumentException("Row ID cannot be null");
        }
        orderRowService.removeOrderRow(id, rowId);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
    @PostMapping("/{id}/rows/{rowId}/edit")
    @Loggable
    public String editOrderRow(@PathVariable Long id,
                               @PathVariable Long rowId,
                               @RequestParam String applianceName,
                               @RequestParam Long quantity) {
        orderRowService.updateOrderRow(id, rowId, applianceName, quantity);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
    @PostMapping("/{id}/rows/editMode")
    @Loggable
    public String enableEditMode(@PathVariable Long id, @RequestParam Long rowId) {
        orderRowService.enableEditMode(id, rowId);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
    @PostMapping("/{id}/rows/update")
    @Loggable
    public String updateOrderRow(@PathVariable Long id,
                                 @RequestParam Long rowId,
                                 @RequestParam String applianceName,
                                 @RequestParam Long quantity) {
        orderRowService.updateOrderRow(id, rowId, applianceName, quantity);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
    @PostMapping("/{id}/rows/cancelEdit")
    @Loggable
    public String cancelEditMode(@PathVariable Long id, @RequestParam Long rowId) {
        orderRowService.cancelEditMode(id, rowId);
        return "redirect:/orders/" + id + "/choiceAppliance";
    }
}