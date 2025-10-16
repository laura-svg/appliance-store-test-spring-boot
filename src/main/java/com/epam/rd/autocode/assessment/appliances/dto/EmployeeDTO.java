package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long Id;

    @NotEmpty(message = "{validation.employee.name.required}")
    @Size(min = 2, max = 100, message = "{validation.employee.name.size}")
    private String name;
    @Email(message = "{validation.employee.email.invalid}")
    @NotEmpty(message = "{validation.employee.email.required}")
    private String email;
    @NotEmpty(message = "{validation.employee.department.required}")
    @Size(min = 3, max = 50, message = "{validation.employee.department.size}")
    private String department;
    @Size(min = 8, message = "{validation.employee.password.size}")
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}", message = "{validation.employee.password.pattern}")
    private String password;
}
