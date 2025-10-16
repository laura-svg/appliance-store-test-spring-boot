package com.epam.rd.autocode.assessment.appliances.mapper.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import org.springframework.stereotype.Component;

@Component
public class ApplianceMapperImpl implements ApplianceMapper {
    @Override
    public ApplianceDTO toDTO(Appliance appliance)  {
        if (appliance == null) {
            return null;
        }
        return ApplianceDTO.builder()
                .id(appliance.getId())
                .name(appliance.getName())
                .category(appliance.getCategory())
                .model(appliance.getModel())
                .manufacturer(appliance.getManufacturer())
                .powerType(appliance.getPowerType())
                .characteristic(appliance.getCharacteristic())
                .description(appliance.getDescription())
                .power(appliance.getPower())
                .price(appliance.getPrice())
                .build();
    }
    public Appliance toEntity(ApplianceDTO dto) {
        if (dto == null) {
            return null;
        }
        Appliance appliance = new Appliance();
        appliance.setId(dto.getId());
        appliance.setName(dto.getName());
        appliance.setCategory(dto.getCategory());
        appliance.setModel(dto.getModel());
        appliance.setManufacturer(dto.getManufacturer());
        appliance.setPowerType(dto.getPowerType());
        appliance.setCharacteristic(dto.getCharacteristic());
        appliance.setDescription(dto.getDescription());
        appliance.setPower(dto.getPower());
        appliance.setPrice(dto.getPrice());
        return appliance;
    }
    @Override
    public void updateEntityFromDTO(ApplianceDTO applianceDTO, Appliance appliance) {
        if (applianceDTO == null || appliance == null) {
            return;
        }
        appliance.setName(applianceDTO.getName());
        appliance.setCategory(applianceDTO.getCategory());
        appliance.setModel(applianceDTO.getModel());
        appliance.setManufacturer(applianceDTO.getManufacturer());
        appliance.setPowerType(applianceDTO.getPowerType());
        appliance.setCharacteristic(applianceDTO.getCharacteristic());
        appliance.setDescription(applianceDTO.getDescription());
        appliance.setPower(applianceDTO.getPower());
        appliance.setPrice(applianceDTO.getPrice());
    }
}