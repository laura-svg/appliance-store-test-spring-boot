package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientDTO {
    private Long id;

    @NotEmpty(message = "{validation.userName.required}")
    @Size(min = 2, max = 100, message = "{validation.userName.size}")
    @Pattern(regexp = "^[^0-9]*$", message = "{validation.userName.invalid}")
    private String userName;
    @Email(message = "{validation.userEmail.invalid}")
    @NotEmpty(message = "{validation.userEmail.required}")
    private String userEmail;
    @Size(min = 8, message = "{validation.userPassword.size}")
    @Pattern(
            regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}",
            message = "{validation.userPassword.pattern}"
    )
    private String userPassword;
    @NotEmpty(message = "{validation.clientCard.required}")
    @Pattern(
            regexp = "\\d{4}-\\d{4}",
            message = "{validation.clientCard.pattern}"
    )
    private String clientCard;
}
