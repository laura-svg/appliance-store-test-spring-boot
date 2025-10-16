package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import org.springframework.data.domain.Page;

public interface ManufacturerService {
    Page<ManufacturerDTO> getManufacturers(int page, int size, String sort, String order);
    ManufacturerDTO getManufacturerById(Long id);
    void createManufacturer(ManufacturerDTO manufacturerDTO);
    void updateManufacturer(ManufacturerDTO manufacturerDTO);
    void deleteManufacturer(Long id);
}