package com.epam.rd.autocode.assessment.appliances.mapper.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ManufacturerDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ManufacturerMapper;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerMapperImpl implements ManufacturerMapper {
    @Override
    public ManufacturerDTO toDTO(Manufacturer manufacturer) {
        if (manufacturer == null) {
            return null;
        }
        return new ManufacturerDTO(manufacturer.getId(), manufacturer.getName());
    }
    @Override
    public Manufacturer toEntity(ManufacturerDTO manufacturerDTO) {
        if (manufacturerDTO == null) {
            return null;
        }
        return new Manufacturer(manufacturerDTO.getId(), manufacturerDTO.getName());
    }
}