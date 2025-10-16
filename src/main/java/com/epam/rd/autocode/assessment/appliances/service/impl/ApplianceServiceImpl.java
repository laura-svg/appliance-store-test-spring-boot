package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApplianceServiceImpl implements ApplianceService {
    private final ApplianceMapper applianceMapper;
    private final ManufacturerMapper manufacturerMapper;
    private final ApplianceRepository applianceRepository;
    private final ManufacturerRepository manufacturerRepository;
    @Override
    @Loggable
    public Optional<ApplianceDTO> findByName(String name) {
        return applianceRepository.findByName(name)
                .map(applianceMapper::toDTO);
    }
    @Override
    @Loggable
    public List<ApplianceDTO> findAll() {
        return applianceRepository.findAll().stream()
                .map(applianceMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Loggable
    public List<ManufacturerDTO> getAllManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(manufacturerMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Loggable
    public List<Category> getAllCategories() {
        return Arrays.asList(Category.values());
    }
    @Override
    @Loggable
    public List<PowerType> getAllPowerTypes() {
        return Arrays.asList(PowerType.values());
    }
    @Override
    @Loggable
    public Optional<ApplianceDTO> findById(Long id) {
        return applianceRepository.findById(id)
                .map(applianceMapper::toDTO);
    }
    @Override
    @Loggable
    public List<ApplianceDTO> getAppliances(String search, String sort, String category, String manufacturer) {
        List<ApplianceDTO> appliances = findAll();
        if (search != null && !search.isEmpty()) {
            appliances = appliances.stream()
                    .filter(appliance -> appliance.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty()) {
            appliances = appliances.stream()
                    .filter(appliance -> appliance.getCategory().name().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (manufacturer != null && !manufacturer.isEmpty()) {
            appliances = appliances.stream()
                    .filter(appliance -> appliance.getManufacturer().getName().equalsIgnoreCase(manufacturer))
                    .collect(Collectors.toList());
        }
        if ("price".equalsIgnoreCase(sort)) {
            appliances = appliances.stream()
                    .sorted(Comparator.comparing(ApplianceDTO::getPrice))
                    .collect(Collectors.toList());
        } else if ("name".equalsIgnoreCase(sort)) {
            appliances = appliances.stream()
                    .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
                    .collect(Collectors.toList());
        }
        return appliances;
    }
    public ApplianceDTO getApplianceForEdit(Long id) {
        Appliance appliance = applianceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appliance Id:" + id));
        return applianceMapper.toDTO(appliance);     }

    public Map<String, Object> getEditFormAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("categories", getAllCategories());
        attributes.put("powerTypes", getAllPowerTypes());
        attributes.put("manufacturers", getAllManufacturers());
        return attributes;
    }
    @Override
    @Loggable
    public List<ApplianceDTO> getSortedAppliances(String sort, String order) {
        List<Appliance> appliances = applianceRepository.findAll(Sort.by(Sort.Direction.fromString(order), sort));
        return appliances.stream()
                .map(applianceMapper::toDTO)
                .collect(Collectors.toList());
    }
}

