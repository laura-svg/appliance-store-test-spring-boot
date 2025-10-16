package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.impl.EmployeeMapperImpl;
import com.epam.rd.autocode.assessment.appliances.mapper.impl.OrderMapper;
import com.epam.rd.autocode.assessment.appliances.mapper.OrderRowMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Getter
public class OrderServiceImpl implements OrderService {
    private final EmployeeRepository employeeRepository;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final Map<Long, Orders> inMemory = new HashMap<>();
    private long idGenerator = 1;
    private final ClientMapper clientMapper;
    private final EmployeeMapperImpl employeeMapperImpl;
    private final OrderRowMapper orderRowMapper;

    @Override
    @Loggable
    public List<OrderDTO> findAll() {
        return inMemory.values().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    @Loggable
    public Optional<OrderDTO> findById(Long id) {
        Orders entity = inMemory.get(id);
        return Optional.ofNullable(OrderMapper.toDto(entity));
    }
    @Override
    @Loggable
    public Optional<Orders> findEntityById(Long id) {
        Orders order = inMemory.get(id);
        if (order != null && order.getOrderRowSet() == null) {
            order.setOrderRowSet(new HashSet<>());
        }
        return Optional.ofNullable(order);
    }
    @Override
    @Loggable
    public OrderDTO createOrder(OrderDTO dto) {
        Orders entity = OrderMapper.toEntity(dto);
        Employee employee = employeeService.findById(dto.getEmployeeId())
                .map(employeeMapperImpl::toEntity)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        Client client = clientService.findById(dto.getClientId())
                .map(clientMapper::toEntity)
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        entity.setEmployee(employee);
        entity.setClient(client);
        entity.setId(idGenerator++);
        inMemory.put(entity.getId(), entity);
        return OrderMapper.toDto(entity);
    }
    @Override
    @Loggable
    public OrderDTO updateOrder(Long id, OrderDTO dto) {
        Orders existing = inMemory.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Order not found");
        }
        EmployeeDTO employeeDTO = employeeService.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));
        ClientDTO clientDTO = clientService.findById(dto.getClientId())
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        existing.setEmployee(employeeMapperImpl.toEntity(employeeDTO));
        existing.setClient(clientMapper.toEntity(clientDTO));
        existing.setApproved(dto.isApproved());
        inMemory.put(id, existing);
        return OrderMapper.toDto(existing);
    }
    @Override
    @Loggable
    public void deleteOrder(Long id) {
        if (!inMemory.containsKey(id)) {
            throw new NoSuchElementException("Order not found");
        }
        inMemory.remove(id);
    }
    @Override
    @Loggable
    public void approveOrder(Long id) {
        Orders existing = inMemory.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Order not found");
        }
        existing.setApproved(true);
        inMemory.put(id, existing);
    }
    @Override
    @Loggable
    public void disapproveOrder(Long id) {
        Orders existing = inMemory.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Order not found");
        }
        existing.setApproved(false);
        inMemory.put(id, existing);
    }
    @Override
    @Loggable
    public List<OrderRowDTO> findOrderRowsByOrderId(Long orderId) {
        Orders order = inMemory.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found");
        }
        if (order.getOrderRowSet() == null) {
            order.setOrderRowSet(new HashSet<>());
        }
        return order.getOrderRowSet().stream()
                .map(orderRowMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Loggable
    public List<OrderDTO> findOrdersWithPagination(int page, int size, String sort, String order) {
        List<OrderDTO> allOrders = findAll();
        Comparator<OrderDTO> comparator;
        switch (sort) {
            case "employeeName":
                comparator = Comparator.comparing(OrderDTO::getEmployeeName);
                break;
            case "clientName":
                comparator = Comparator.comparing(OrderDTO::getClientName);
                break;
            case "amount":
                comparator = Comparator.comparing(OrderDTO::getAmount);
                break;
            default:
                comparator = Comparator.comparing(OrderDTO::getId);
                break;
        }
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        allOrders.sort(comparator);
        int fromIndex = Math.min(page * size, allOrders.size());
        int toIndex = Math.min((page + 1) * size, allOrders.size());
        return allOrders.subList(fromIndex, toIndex);
    }
}