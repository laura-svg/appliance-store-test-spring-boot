package com.epam.rd.autocode.assessment.appliances.controller.security;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ClientRepository clientRepository;

    @GetMapping("/change-role")
    public String showChangeRoleForm(@RequestParam Long clientId, Model model) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(client.getId());
        employeeDTO.setName(client.getName());
        employeeDTO.setEmail(client.getEmail());
        model.addAttribute("employeeDTO", employeeDTO);
        return "change-role";
    }
    @PostMapping("/change-role")
    public String changeRole(@ModelAttribute @Valid EmployeeDTO employeeDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "change-role";
        }
        userService.changeClientToEmployee(employeeDTO);
        return "redirect:/admin";
    }
}