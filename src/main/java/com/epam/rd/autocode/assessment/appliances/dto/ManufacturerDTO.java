package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerDTO {
    private Long id;

    @NotEmpty(message = "{validation.manufacturer.name.required}")
    @Size(min = 2, message = "{validation.manufacturer.name.size}")
    private String name;
}
