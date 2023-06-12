package com.example.carsharingservice.dto.request;

import lombok.Data;

@Data
public class UserRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public UserRequestDto(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
