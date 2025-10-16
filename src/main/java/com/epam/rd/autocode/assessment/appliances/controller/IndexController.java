package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    private final ApplianceService applianceService;
    public IndexController(ApplianceService applianceService) {
        this.applianceService = applianceService;
    }

    @GetMapping("/")
    @Loggable
    public String getMain(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) String powerType,
            @RequestParam(required = false) Integer powerMin,
            @RequestParam(required = false) Integer powerMax,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            Model model
    ) {
        List<ApplianceDTO> all = applianceService.findAll();
        if (search != null && !search.isEmpty()) {
            String lower = search.toLowerCase();
            all = all.stream()
                    .filter(a -> {
                        boolean byName = (a.getName() != null && a.getName().toLowerCase().contains(lower));
                        boolean byModel = (a.getModel() != null && a.getModel().toLowerCase().contains(lower));
                        boolean byChar = (a.getCharacteristic() != null && a.getCharacteristic().toLowerCase().contains(lower));
                        boolean byDesc = (a.getDescription() != null && a.getDescription().toLowerCase().contains(lower));
                        boolean byManu = (a.getManufacturer() != null && a.getManufacturer().getName() != null
                                && a.getManufacturer().getName().toLowerCase().contains(lower));
                        return byName || byModel || byChar || byDesc || byManu;
                    })
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            all = all.stream()
                    .filter(a -> a.getCategory() != null
                            && a.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (manufacturerId != null) {
            all = all.stream()
                    .filter(a -> a.getManufacturer() != null
                            && a.getManufacturer().getId().equals(manufacturerId))
                    .collect(Collectors.toList());
        }
        if (powerType != null && !powerType.isEmpty()) {
            all = all.stream()
                    .filter(a -> a.getPowerType() != null
                            && a.getPowerType().name().equalsIgnoreCase(powerType))
                    .collect(Collectors.toList());
        }
        if (powerMin != null) {
            all = all.stream()
                    .filter(a -> a.getPower() != null && a.getPower() >= powerMin)
                    .collect(Collectors.toList());
        }
        if (powerMax != null) {
            all = all.stream()
                    .filter(a -> a.getPower() != null && a.getPower() <= powerMax)
                    .collect(Collectors.toList());
        }
        if (priceMin != null) {
            all = all.stream()
                    .filter(a -> a.getPrice() != null && a.getPrice().compareTo(priceMin) >= 0)
                    .collect(Collectors.toList());
        }
        if (priceMax != null) {
            all = all.stream()
                    .filter(a -> a.getPrice() != null && a.getPrice().compareTo(priceMax) <= 0)
                    .collect(Collectors.toList());
        }
        int initialLimit = 9;
        List<ApplianceDTO> firstBatch = all.stream().limit(initialLimit).toList();
        model.addAttribute("appliances", firstBatch);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("loadedCount", firstBatch.size());
        model.addAttribute("search", search);
        model.addAttribute("category", category);
        model.addAttribute("manufacturerId", manufacturerId);
        model.addAttribute("powerType", powerType);
        model.addAttribute("powerMin", powerMin);
        model.addAttribute("powerMax", powerMax);
        model.addAttribute("priceMin", priceMin);
        model.addAttribute("priceMax", priceMax);
        model.addAttribute("categories", List.of(Category.BIG, Category.SMALL));
        model.addAttribute("manufacturers", applianceService.getAllManufacturers());
        model.addAttribute("powerTypes", List.of(PowerType.ACCUMULATOR, PowerType.AC110, PowerType.AC220));
        return "index";
    }
    @GetMapping("/loadMore")
    @Loggable
    @ResponseBody
    public List<ApplianceDTO> loadMore(
            @RequestParam int fromIndex,
            @RequestParam int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long manufacturerId,
            @RequestParam(required = false) String powerType,
            @RequestParam(required = false) Integer powerMin,
            @RequestParam(required = false) Integer powerMax,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax
    ) {
        List<ApplianceDTO> all = applianceService.findAll();
        if (search != null && !search.isEmpty()) {
            String lower = search.toLowerCase();
            all = all.stream()
                    .filter(a -> {
                        boolean byName = (a.getName() != null && a.getName().toLowerCase().contains(lower));
                        boolean byModel = (a.getModel() != null && a.getModel().toLowerCase().contains(lower));
                        boolean byChar = (a.getCharacteristic() != null && a.getCharacteristic().toLowerCase().contains(lower));
                        boolean byDesc = (a.getDescription() != null && a.getDescription().toLowerCase().contains(lower));
                        boolean byManu = (a.getManufacturer() != null && a.getManufacturer().getName() != null
                                && a.getManufacturer().getName().toLowerCase().contains(lower));
                        return byName || byModel || byChar || byDesc || byManu;
                    })
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            all = all.stream()
                    .filter(a -> a.getCategory() != null
                            && a.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (manufacturerId != null) {
            all = all.stream()
                    .filter(a -> a.getManufacturer() != null
                            && a.getManufacturer().getId().equals(manufacturerId))
                    .collect(Collectors.toList());
        }
        if (powerType != null && !powerType.isEmpty()) {
            all = all.stream()
                    .filter(a -> a.getPowerType() != null
                            && a.getPowerType().name().equalsIgnoreCase(powerType))
                    .collect(Collectors.toList());
        }
        if (powerMin != null) {
            all = all.stream()
                    .filter(a -> a.getPower() != null && a.getPower() >= powerMin)
                    .collect(Collectors.toList());
        }
        if (powerMax != null) {
            all = all.stream()
                    .filter(a -> a.getPower() != null && a.getPower() <= powerMax)
                    .collect(Collectors.toList());
        }
        if (priceMin != null) {
            all = all.stream()
                    .filter(a -> a.getPrice() != null && a.getPrice().compareTo(priceMin) >= 0)
                    .collect(Collectors.toList());
        }
        if (priceMax != null) {
            all = all.stream()
                    .filter(a -> a.getPrice() != null && a.getPrice().compareTo(priceMax) <= 0)
                    .collect(Collectors.toList());
        }

        int toIndex = Math.min(fromIndex + limit, all.size());
        if (fromIndex >= all.size()) {
            return List.of();
        }
        return all.subList(fromIndex, toIndex);
    }
    @GetMapping("/autocomplete")
    @Loggable
    @ResponseBody
    public List<String> autocomplete(@RequestParam String query) {
        List<ApplianceDTO> all = applianceService.findAll();
        String lower = query.toLowerCase();
        return all.stream()
                .filter(a -> a.getName() != null && a.getName().toLowerCase().contains(lower))
                .map(ApplianceDTO::getName)
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }
}