package com.epam.rd.autocode.assessment.appliances.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailsDTO {
    private Long orderId;
    private String clientName;
    private String employeeName;
    private Boolean approved;
    private BigDecimal totalAmount;
    private List<OrderRowDTO> orderRows;
}