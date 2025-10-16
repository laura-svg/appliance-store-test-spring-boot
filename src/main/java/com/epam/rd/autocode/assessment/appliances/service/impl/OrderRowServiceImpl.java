package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.OrderRowMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.OrderRowService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderRowServiceImpl implements OrderRowService {
    private Long temporaryIdCounter = 1L;
    private final ApplianceService applianceService;
    private final OrderService orderService;
    private final OrderRowMapper orderRowMapper;
    private final ApplianceMapper applianceMapper;
    private final Map<Long, Long> editModes = new HashMap<>();

    @Override
    @Loggable
    public void addOrderRow(Long orderId, String applianceName, Long quantity) {
        Orders order = orderService.findEntityById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        ApplianceDTO applianceDTO = applianceService.findByName(applianceName)
                .orElseThrow(() -> new NoSuchElementException("Appliance with name '" + applianceName + "' not found"));
        Appliance appliance = applianceMapper.toEntity(applianceDTO);
        OrderRow orderRow = new OrderRow();
        orderRow.setId(temporaryIdCounter++);
        orderRow.setAppliance(appliance);
        orderRow.setNumber(quantity);
        orderRow.setAmount(appliance.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.getOrderRowSet().add(orderRow);
    }
    @Override
    @Loggable
    public List<OrderRowDTO> findAllByOrderId(Long orderId) {
        Orders order = orderService.findEntityById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        return Optional.ofNullable(order.getOrderRowSet())
                .orElse(Collections.emptySet())
                .stream()
                .map(orderRow -> {
                    OrderRowDTO dto = orderRowMapper.toDTO(orderRow);
                    if (orderRow.getAppliance() != null) {
                        dto.setApplianceName(orderRow.getAppliance().getName());
                    } else {
                        dto.setApplianceName("Unknown Appliance");
                    }
                    dto.setEditMode(editModes.get(orderId) != null && editModes.get(orderId).equals(orderRow.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    @Loggable
    public void removeOrderRow(Long orderId, Long orderRowId) {
        Orders order = orderService.findEntityById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        boolean removed = order.getOrderRowSet().removeIf(row -> Objects.equals(row.getId(), orderRowId));
        if (!removed) {
            throw new NoSuchElementException("OrderRow not found");
        }
        if (editModes.get(orderId) != null && editModes.get(orderId).equals(orderRowId)) {
            editModes.remove(orderId);
        }
    }
    @Override
    @Loggable
    public void enableEditMode(Long orderId, Long rowId) {
        Orders order = orderService.findEntityById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        boolean rowExists = order.getOrderRowSet().stream()
                .anyMatch(row -> row.getId().equals(rowId));
        if (!rowExists) {
            throw new NoSuchElementException("OrderRow not found");
        }
        editModes.put(orderId, rowId);
    }
    @Override
    @Loggable
    public void cancelEditMode(Long orderId, Long rowId) {
        if (editModes.get(orderId) != null && editModes.get(orderId).equals(rowId)) {
            editModes.remove(orderId);
        }
    }
    @Override
    @Loggable
    public void updateOrderRow(Long orderId, Long rowId, String applianceName, Long quantity) {
        Orders order = orderService.findEntityById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        OrderRow orderRow = order.getOrderRowSet().stream()
                .filter(row -> row.getId().equals(rowId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("OrderRow with ID " + rowId + " not found"));
        ApplianceDTO applianceDTO = applianceService.findByName(applianceName)
                .orElseThrow(() -> new NoSuchElementException("Appliance with name '" + applianceName + "' not found"));
        Appliance appliance = applianceMapper.toEntity(applianceDTO);
        orderRow.setAppliance(appliance);
        orderRow.setNumber(quantity);
        orderRow.setAmount(appliance.getPrice().multiply(BigDecimal.valueOf(quantity)));
    }
}