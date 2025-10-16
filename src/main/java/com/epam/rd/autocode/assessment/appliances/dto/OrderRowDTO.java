package com.epam.rd.autocode.assessment.appliances.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRowDTO {
    private Long id;
    private String applianceName;
    private ApplianceDTO appliance;
    private Long number;
    private BigDecimal amount;
    private boolean editMode;
}




