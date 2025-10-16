package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "appliance_id")
    private Appliance appliance;
    private Long number;
    private BigDecimal amount;
    public OrderRow(Long number, Appliance appliance, Long id, BigDecimal amount) {
        this.number = number;
        this.appliance = appliance;
        this.id = id;
        this.amount = amount;
    }
}