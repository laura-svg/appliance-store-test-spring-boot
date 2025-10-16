package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import java.util.List;

public interface OrderRowService {
    void addOrderRow(Long orderId, String applianceName, Long quantity);
    List<OrderRowDTO> findAllByOrderId(Long orderId);
    void removeOrderRow(Long orderId, Long orderRowId);
    void updateOrderRow(Long orderId, Long rowId, String applianceName, Long quantity);
    void enableEditMode(Long orderId, Long rowId);
    void cancelEditMode(Long orderId, Long rowId);
}