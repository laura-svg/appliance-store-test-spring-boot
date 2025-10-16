package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@Controller
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private ClientService clientService;

    @GetMapping
    @Loggable
    public String getClientsMain(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Model model) {
        Page<ClientDTO> clients = clientService.getClients(page, size, sort, order);
        int totalPages = clients.getTotalPages();
        if (totalPages == 0) {
            model.addAttribute("clients", Collections.emptyList());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
        } else {
            model.addAttribute("clients", clients.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
        }
        model.addAttribute("totalItems", clients.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "client/clients";
    }
    @GetMapping("/add")
    @Loggable
    public String getNewClients(Model model) {
        model.addAttribute("client", new ClientDTO());
        return "client/newClient";
    }
    @PostMapping("/add-client")
    @Loggable
    public String postNewClient(@Valid @ModelAttribute("client") ClientDTO clientDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "client/newClient";
        }
        clientService.saveClient(clientDTO);
        return "redirect:/clients";
    }
    @GetMapping("/{id}/delete")
    @Loggable
    public String deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients";
    }
    @GetMapping("/{id}/edit")
    @Loggable
    public String editClient(@PathVariable("id") Long id, Model model) {
        ClientDTO clientDTO = clientService.getClientDTOById(id);
        logger.info("Loaded ClientDTO for editing: {}", clientDTO);
        model.addAttribute("clientDTO", clientDTO);
        return "client/editClient";
    }
    @PostMapping("/update")
    @Loggable
    public String updateClient(@Valid @ModelAttribute("clientDTO") ClientDTO clientDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "client/editClient";
        }
        if (clientDTO.getUserPassword() == null || clientDTO.getUserPassword().isEmpty()) {
            ClientDTO existingClient = clientService.getClientDTOById(clientDTO.getId());
            clientDTO.setUserPassword(existingClient.getUserPassword());
        }
        clientService.saveClient(clientDTO);
        logger.info("Saved updated client: {}", clientDTO);
        return "redirect:/clients";
    }
}