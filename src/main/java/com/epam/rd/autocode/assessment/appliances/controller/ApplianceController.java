package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/appliances")
public class ApplianceController {
    private ApplianceRepository repository;
    private ApplianceService service;
    private ApplianceMapper applianceMapper;

    @GetMapping
    @Loggable
    public String getApplianceMain(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Model model) {
        List<ApplianceDTO> appliances = service.getSortedAppliances(sort, order);
        int totalPages = (int) Math.ceil((double) appliances.size() / size);
        int fromIndex = Math.min(page * size, appliances.size());
        int toIndex = Math.min((page + 1) * size, appliances.size());
        model.addAttribute("appliances", appliances.subList(fromIndex, toIndex));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "appliance/appliances";
    }
    @GetMapping("/add")
    @Loggable
    public String showAddApplianceForm(Model model) {
        model.addAttribute("appliance", new ApplianceDTO());
        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("powerTypes", service.getAllPowerTypes());
        model.addAttribute("manufacturers", service.getAllManufacturers());
        return "appliance/newAppliance";
    }
    @PostMapping("/add-appliance")
    @Loggable
    public String createNewAppliance(@Valid @ModelAttribute("appliance") ApplianceDTO applianceDTO,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", service.getAllCategories());
            model.addAttribute("powerTypes", service.getAllPowerTypes());
            model.addAttribute("manufacturers", service.getAllManufacturers());
            return "appliance/newAppliance";
        }
        Appliance appliance = applianceMapper.toEntity(applianceDTO);
        repository.save(appliance);
        return "redirect:/appliances";
    }
    @GetMapping("/{id}/delete")
    @Loggable
    public String deleteAppliance(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/appliances";
    }
    @GetMapping("/{id}/edit")
    @Loggable
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Appliance appliance = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appliance Id: " + id));
        ApplianceDTO applianceDTO = applianceMapper.toDTO(appliance);
        model.addAttribute("appliance", applianceDTO);
        model.addAttribute("categories", service.getAllCategories());
        model.addAttribute("powerTypes", service.getAllPowerTypes());
        model.addAttribute("manufacturers", service.getAllManufacturers());
        return "appliance/editAppliance";
    }
    @PostMapping("/{id}/update")
    @Loggable
    public String updateAppliance(@PathVariable("id") Long id,
                                  @Valid @ModelAttribute("appliance") ApplianceDTO applianceDTO,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", service.getAllCategories());
            model.addAttribute("powerTypes", service.getAllPowerTypes());
            model.addAttribute("manufacturers", service.getAllManufacturers());
            return "appliance/editAppliance";
        }
        Appliance existingAppliance = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appliance Id: " + id));
        applianceMapper.updateEntityFromDTO(applianceDTO, existingAppliance);
        repository.save(existingAppliance);
        return "redirect:/appliances";
    }
}