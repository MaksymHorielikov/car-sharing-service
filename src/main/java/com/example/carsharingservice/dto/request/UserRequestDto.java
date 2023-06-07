package com.example.carsharingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "cannot blank")
    private String email;
    @NotBlank(message = "cannot blank")
    private String firstName;
    @NotBlank(message = "cannot blank")
    private String lastName;
    @NotBlank(message = "cannot blank")
    private String password;
}
