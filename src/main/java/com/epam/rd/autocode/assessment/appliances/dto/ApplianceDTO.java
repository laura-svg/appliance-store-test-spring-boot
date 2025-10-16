package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceDTO {
    private Long id;

    @NotBlank(message = "{appliance.name.required}")
    @Size(max = 100, message = "{appliance.name.size}")
    private String name;
    @NotNull(message = "{appliance.category.required}")
    private Category category;
    @NotBlank(message = "{appliance.model.required}")
    @Size(max = 50, message = "{appliance.model.size}")
    private String model;
    @NotNull(message = "{appliance.manufacturer.required}")
    private Manufacturer manufacturer;
    @NotNull(message = "{appliance.powerType.required}")
    private PowerType powerType;
    @Size(max = 255, min = 3, message = "{appliance.characteristic.size}")
    private String characteristic;
    @Size(max = 500, message = "{appliance.description.size}")
    private String description;
    @NotNull(message = "{appliance.power.required}")
    @Min(value = 1, message = "{appliance.power.min}")
    @Max(value = 10000, message = "{appliance.power.max}")
    private Integer power;
    @NotNull(message = "{appliance.price.required}")
    @DecimalMin(value = "0.01", message = "{appliance.price.min}")
    private BigDecimal price;
    public String getFormattedPrice() {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(price);
    }
}