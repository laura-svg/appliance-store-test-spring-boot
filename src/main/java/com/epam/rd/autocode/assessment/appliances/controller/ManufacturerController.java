package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @GetMapping
    @Loggable
    public String getManufacturerMain(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order,
            Model model) {
        Page<ManufacturerDTO> manufacturers = manufacturerService.getManufacturers(page, size, sort, order);
        model.addAttribute("manufacturers", manufacturers.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", manufacturers.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        return "manufacture/manufacturers";
    }
    @GetMapping("/add")
    @Loggable
    public String showNewManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new ManufacturerDTO());
        return "manufacture/newManufacturer";
    }
    @PostMapping("/add-manufacturer")
    @Loggable
    public String createNewManufacturer(@Valid @ModelAttribute("manufacturer") ManufacturerDTO manufacturerDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "manufacture/newManufacturer";
        }
        manufacturerService.createManufacturer(manufacturerDTO);
        return "redirect:/manufacturers";
    }
    @GetMapping("/{id}/edit")
    @Loggable
    public String getEditManufacturerForm(@PathVariable("id") Long id, Model model) {
        ManufacturerDTO manufacturerDTO = manufacturerService.getManufacturerById(id);
        if (manufacturerDTO == null) {
            return "redirect:/manufacturers?error=notfound";
        }
        model.addAttribute("manufacturer", manufacturerDTO);
        return "manufacture/editManufacturer";
    }
    @PostMapping("/update")
    @Loggable
    public String updateManufacturer(
            @Valid @ModelAttribute("manufacturer") ManufacturerDTO manufacturerDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            return "manufacture/editManufacturer";
        }
        manufacturerService.updateManufacturer(manufacturerDTO);
        return "redirect:/manufacturers";
    }
    @GetMapping("/{id}/delete")
    @Loggable
    public String deleteManufacturer(@PathVariable("id") Long id) {
        manufacturerService.deleteManufacturer(id);
        return "redirect:/manufacturers";
    }
}