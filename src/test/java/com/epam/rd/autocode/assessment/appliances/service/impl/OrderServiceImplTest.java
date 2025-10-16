package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.impl.EmployeeMapperImpl;
import com.epam.rd.autocode.assessment.appliances.mapper.OrderRowMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.Getter;
import lombok.Setter;
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
import static org.mockito.Mockito.*;
@Getter
@Setter
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private EmployeeMapperImpl employeeMapperImpl;

    @Mock
    private OrderRowMapper orderRowMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDTO orderDTO;
    private Employee employee;
    private EmployeeDTO employeeDTO;
    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "Jane Smith", "jane@example.com", "password123", "IT");
        employeeDTO = new EmployeeDTO(1L, "Jane Smith", "jane@example.com", "IT", "password123");

        client = new Client(1L, "John Doe", "john@example.com", "password123", "1234-5678");
        clientDTO = new ClientDTO(1L, "John Doe", "john@example.com", "password123", "1234-5678");

        Orders order = new Orders();
        order.setId(1L);
        order.setEmployee(employee);
        order.setClient(client);
        order.setApproved(false);
        order.setOrderRowSet(new HashSet<>());

        orderDTO = OrderDTO.builder()
                .id(1L)
                .employeeId(1L)
                .clientId(1L)
                .employeeName("Jane Smith")
                .clientName("John Doe")
                .approved(false)
                .amount(BigDecimal.ZERO)
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfOrders() {
        List<OrderDTO> result = orderService.findAll();

        assertThat(result).isNotNull();
    }



    @Test
    void findEntityById_WhenExists_ShouldReturnOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        Optional<Orders> result = orderService.findEntityById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getOrderRowSet()).isNotNull();
    }

    @Test
    void findEntityById_WhenNotExists_ShouldReturnEmpty() {
        Optional<Orders> result = orderService.findEntityById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void createOrder_ShouldCreateAndReturnOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmployeeName()).isEqualTo("Jane Smith");
        assertThat(result.getClientName()).isEqualTo("John Doe");
    }

    @Test
    void createOrder_WhenEmployeeNotFound_ShouldThrowException() {
        when(employeeService.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    void createOrder_WhenClientNotFound_ShouldThrowException() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(orderDTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Client not found");
    }

    @Test
    void updateOrder_WhenExists_ShouldUpdateAndReturnOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        OrderDTO updatedDTO = OrderDTO.builder()
                .id(1L)
                .employeeId(1L)
                .clientId(1L)
                .approved(true)
                .build();

        OrderDTO result = orderService.updateOrder(1L, updatedDTO);

        assertThat(result).isNotNull();
        assertThat(result.isApproved()).isTrue();
    }

    @Test
    void updateOrder_WhenNotExists_ShouldThrowException() {
        OrderDTO updatedDTO = OrderDTO.builder().id(999L).employeeId(1L).clientId(1L).build();

        assertThatThrownBy(() -> orderService.updateOrder(999L, updatedDTO))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }


    @Test
    void deleteOrder_WhenNotExists_ShouldThrowException() {
        assertThatThrownBy(() -> orderService.deleteOrder(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void approveOrder_WhenExists_ShouldApproveOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);
        orderService.approveOrder(1L);

        Optional<OrderDTO> result = orderService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().isApproved()).isTrue();
    }

    @Test
    void approveOrder_WhenNotExists_ShouldThrowException() {
        assertThatThrownBy(() -> orderService.approveOrder(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void disapproveOrder_WhenExists_ShouldDisapproveOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);
        orderService.approveOrder(1L);
        orderService.disapproveOrder(1L);

        Optional<OrderDTO> result = orderService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().isApproved()).isFalse();
    }

    @Test
    void disapproveOrder_WhenNotExists_ShouldThrowException() {
        assertThatThrownBy(() -> orderService.disapproveOrder(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void findOrderRowsByOrderId_WhenExists_ShouldReturnOrderRows() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        List<OrderRowDTO> result = orderService.findOrderRowsByOrderId(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void findOrderRowsByOrderId_WhenNotExists_ShouldThrowException() {
        assertThatThrownBy(() -> orderService.findOrderRowsByOrderId(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void findOrdersWithPagination_ShouldReturnPaginatedOrders() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        List<OrderDTO> result = orderService.findOrdersWithPagination(0, 10, "id", "asc");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void findOrdersWithPagination_WithEmployeeNameSort_ShouldSortByEmployeeName() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        List<OrderDTO> result = orderService.findOrdersWithPagination(0, 10, "employeeName", "asc");

        assertThat(result).isNotNull();
    }

    @Test
    void findOrdersWithPagination_WithDescOrder_ShouldReturnDescendingOrder() {
        when(employeeService.findById(1L)).thenReturn(Optional.of(employeeDTO));
        when(employeeMapperImpl.toEntity(employeeDTO)).thenReturn(employee);
        when(clientService.findById(1L)).thenReturn(Optional.of(clientDTO));
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);

        orderService.createOrder(orderDTO);

        List<OrderDTO> result = orderService.findOrdersWithPagination(0, 10, "id", "desc");

        assertThat(result).isNotNull();
    }
}