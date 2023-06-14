package com.example.carsharingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "cannot blank")
    private String email;
    @NotBlank(message = "cannot blank")
    private String password;
}
