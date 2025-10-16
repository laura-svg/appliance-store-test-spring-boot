package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.OrderDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> findOrdersWithPagination(int page, int size, String sort, String order);
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Long id);
    Optional<Orders>findEntityById(Long id);
    OrderDTO createOrder(OrderDTO dto);
    OrderDTO updateOrder(Long id, OrderDTO dto);
    void deleteOrder(Long id);
    void approveOrder(Long id);
    void disapproveOrder(Long id);
    List<OrderRowDTO> findOrderRowsByOrderId(Long orderId);
}