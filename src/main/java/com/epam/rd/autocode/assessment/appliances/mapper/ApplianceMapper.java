package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;

public interface ApplianceMapper {
    ApplianceDTO toDTO(Appliance appliance);
    Appliance toEntity(ApplianceDTO applianceDTO);
    void updateEntityFromDTO(ApplianceDTO applianceDTO, Appliance appliance);
}