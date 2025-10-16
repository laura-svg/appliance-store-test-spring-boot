package com.epam.rd.autocode.assessment.appliances.mapper.impl;

import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.OrderRowMapper;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderRowMapperImpl implements OrderRowMapper {
    private ApplianceMapper applianceMapper;

    public OrderRowDTO toDTO(OrderRow orderRow) {
        return new OrderRowDTO(
                orderRow.getId(),
                orderRow.getAppliance().getName(),
                applianceMapper.toDTO(orderRow.getAppliance()),
                orderRow.getNumber(),
                orderRow.getAmount(),
                false
        );
    }
    public OrderRow toEntity(OrderRowDTO dto) {
        OrderRow entity = new OrderRow();
        entity.setId(dto.getId());
        entity.setAppliance(applianceMapper.toEntity(dto.getAppliance()));
        entity.setNumber(dto.getNumber());
        entity.setAmount(dto.getAmount());
        return entity;
    }
}