package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;

public interface OrderRowMapper {
    OrderRowDTO toDTO(OrderRow orderRow);
    OrderRow toEntity(OrderRowDTO dto);
}
