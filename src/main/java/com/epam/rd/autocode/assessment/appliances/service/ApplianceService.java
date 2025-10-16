package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApplianceService {
    List<ApplianceDTO> getSortedAppliances(String sort, String order);
    Map<String, Object> getEditFormAttributes();
    ApplianceDTO getApplianceForEdit(Long id);
    Optional<ApplianceDTO> findByName(String name);
    List<ApplianceDTO> findAll();
    List<ManufacturerDTO> getAllManufacturers();
    List<Category> getAllCategories();
    List<PowerType> getAllPowerTypes();
    Optional<ApplianceDTO> findById(Long id);
    List<ApplianceDTO> getAppliances(String search, String sort, String category, String manufacturer);
}

