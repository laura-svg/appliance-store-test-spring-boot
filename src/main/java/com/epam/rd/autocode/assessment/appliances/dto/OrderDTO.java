package com.epam.rd.autocode.assessment.appliances.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long employeeId;
    private Long clientId;
    private String employeeName;
    private String clientName;
    private boolean approved;
    private Set<OrderRowDTO> orderRows = new HashSet<>();
    private BigDecimal amount;
}