package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {
    private ApplianceService applianceService;

    @GetMapping("/{id}")
    @Loggable
    public String viewProduct(@PathVariable Long id, Model model) {
        Optional<ApplianceDTO> applianceOpt = applianceService.findById(id);
        if (applianceOpt.isPresent()) {
            model.addAttribute("appliance", applianceOpt.get());
            return "appliance/productDetail";
        } else {
            model.addAttribute("errorMessage", "Product not found");
            return "error/404";
        }
    }
}