package com.example.carsharingservice.securuty;

import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.exception.AuthenticationException;
import com.example.carsharingservice.model.User;

public interface AuthenticationService {
    User register(UserRequestDto userRequestDto);

    User login(String login, String password) throws AuthenticationException;
}
