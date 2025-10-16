package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ApplianceMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.OrderRowMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRowServiceImplTest {

    @Mock
    private ApplianceService applianceService;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderRowMapper orderRowMapper;

    @Mock
    private ApplianceMapper applianceMapper;

    @InjectMocks
    private OrderRowServiceImpl orderRowService;

    private Orders order;
    private Appliance appliance;
    private ApplianceDTO applianceDTO;
    private Manufacturer manufacturer;

    @BeforeEach
    void setUp() {
        manufacturer = new Manufacturer(1L, "Samsung");

        appliance = new Appliance();
        appliance.setId(1L);
        appliance.setName("Refrigerator");
        appliance.setPrice(new BigDecimal("1200.00"));
        appliance.setManufacturer(manufacturer);
        appliance.setCategory(Category.BIG);

        applianceDTO = ApplianceDTO.builder()
                .id(1L)
                .name("Refrigerator")
                .price(new BigDecimal("1200.00"))
                .manufacturer(manufacturer)
                .category(Category.BIG)
                .build();

        order = new Orders();
        order.setId(1L);
        order.setOrderRowSet(new HashSet<>());
    }

    @Test
    void addOrderRow_ShouldAddOrderRowToOrder() {
        when(orderService.findEntityById(1L)).thenReturn(Optional.of(order));
        when(applianceService.findByName("Refrigerator")).thenReturn(Optional.of(applianceDTO));
        when(applianceMapper.toEntity(applianceDTO)).thenReturn(appliance);

        orderRowService.addOrderRow(1L, "Refrigerator", 2L);

        assertThat(order.getOrderRowSet()).hasSize(1);
        verify(orderService).findEntityById(1L);
        verify(applianceService).findByName("Refrigerator");
    }

    @Test
    void addOrderRow_WhenOrderNotFound_ShouldThrowException() {
        when(orderService.findEntityById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderRowService.addOrderRow(999L, "Refrigerator", 2L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void addOrderRow_WhenApplianceNotFound_ShouldThrowException() {
        when(orderService.findEntityById(1L)).thenReturn(Optional.of(order));
        when(applianceService.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderRowService.addOrderRow(1L, "NonExistent", 2L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Appliance with name 'NonExistent' not found");
    }



    @Test
    void findAllByOrderId_WhenOrderNotFound_ShouldThrowException() {
        when(orderService.findEntityById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderRowService.findAllByOrderId(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void findAllByOrderId_WithNullOrderRowSet_ShouldReturnEmptyList() {
        order.setOrderRowSet(null);
        when(orderService.findEntityById(1L)).thenReturn(Optional.of(order));

        List<OrderRowDTO> result = orderRowService.findAllByOrderId(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void removeOrderRow_ShouldRemoveOrderRow() {
        when(orderService.findEntityById(1L)).thenReturn(Optional.of(order));
        when(applianceService.findByName("Refrigerator")).thenReturn(Optional.of(applianceDTO));
        when(applianceMapper.toEntity(applianceDTO)).thenReturn(appliance);

        orderRowService.addOrderRow(1L, "Refrigerator", 2L);
        Long rowId = order.getOrderRowSet().iterator().next().getId();

        orderRowService.removeOrderRow(1L, rowId);

        assertThat(order.getOrderRowSet()).isEmpty();
    }

    @Test
    void removeOrderRow_WhenOrderNotFound_ShouldThrowException() {
        when(orderService.findEntityById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderRowService.removeOrderRow(999L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void removeOrderRow_WhenOrderRowNotFound_ShouldThrowException() {
        when(orderService.findEntityById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderRowService.removeOrderRow(1L, 999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("OrderRow not found");
    }



    @Test
    void enableEditMode_WhenOrderNotFound_ShouldThrowException() {
        when(orderService.findEntityById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderRowService.enableEditMode(999L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

}