package com.epam.rd.autocode.assessment.appliances.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Table(name = "appliance")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "model")
    private String model;
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;
    @Column(name = "power_type")
    @Enumerated(EnumType.STRING)
    private PowerType powerType;
    @Column(name = "characteristic")
    private String characteristic;
    @Column(name = "description")
    private String description;
    @Column(name = "power", nullable = false)
    private Integer power;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
