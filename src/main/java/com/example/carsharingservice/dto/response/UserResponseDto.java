package com.example.carsharingservice.dto.response;

import com.example.carsharingservice.model.User;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id,
                           String email,
                           String firstName,
                           String lastName,
                           User.Role role
    ) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
