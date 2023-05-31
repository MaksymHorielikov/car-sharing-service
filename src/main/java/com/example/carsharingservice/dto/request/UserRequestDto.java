package com.example.carsharingservice.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
