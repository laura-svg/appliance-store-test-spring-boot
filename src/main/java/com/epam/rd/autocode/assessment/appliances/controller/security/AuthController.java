package com.epam.rd.autocode.assessment.appliances.controller.security;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.LoginDTO;
import com.epam.rd.autocode.assessment.appliances.service.impl.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "registered", required = false) String registered, Model model) {
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please log in.");
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("clientDTO", new ClientDTO());
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute @Valid ClientDTO clientDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.registerClient(clientDTO);
        } catch (IllegalArgumentException e) {
            result.rejectValue("userEmail", "error.client", e.getMessage());
            return "auth/register";
        }
        return "redirect:/login?registered";
    }
}