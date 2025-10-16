package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;

public interface ManufacturerMapper {
    ManufacturerDTO toDTO(Manufacturer manufacturer);
    Manufacturer toEntity(ManufacturerDTO manufacturerDTO);
}