package com.epam.rd.autocode.assessment.appliances.mapper.impl;

import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import java.math.BigDecimal;

public class OrderMapper {
    public static OrderDTO toDto(Orders order) {
        return OrderDTO.builder()
                .id(order.getId())
                .employeeId(order.getEmployee() != null ? order.getEmployee().getId() : null)
                .clientId(order.getClient() != null ? order.getClient().getId() : null)
                .employeeName(order.getEmployee() != null ? order.getEmployee().getName() : "Unknown Employee")
                .clientName(order.getClient() != null ? order.getClient().getName() : "Unknown Client")
                .approved(order.getApproved() != null && order.getApproved())
                .amount(order.getOrderRowSet() != null
                        ? order.getOrderRowSet().stream()
                        .map(OrderRow::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        : BigDecimal.ZERO)
                .build();
    }
    public static Orders toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        Orders order = new Orders();
        order.setId(dto.getId());
        order.setApproved(dto.isApproved());
        return order;
    }
}