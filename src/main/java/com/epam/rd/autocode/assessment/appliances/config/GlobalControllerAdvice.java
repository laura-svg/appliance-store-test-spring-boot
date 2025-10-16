package com.epam.rd.autocode.assessment.appliances.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute
    public void addHttpServletRequest(HttpServletRequest request, Model model) {
        model.addAttribute("httpServletRequest", request);
    }
}