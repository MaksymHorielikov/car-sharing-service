package com.example.carsharingservice.controller;

import com.example.carsharingservice.security.AuthenticationService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.dto.mapper.UserMapper;
import com.example.carsharingservice.dto.response.UserResponseDto;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class UserControllerTest {
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, userMapper, authenticationService);
    }

    @Test
    void testValidAuthentication() {
        User user = new User("test@example.com", "John", "Doe", User.Role.CUSTOMER);
        user.setId(1L);

        UserResponseDto userResponseDto = new UserResponseDto(1L, "test@example.com", "John", "Doe", User.Role.CUSTOMER);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null);

        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto response = userController.get(authentication);

        assertEquals(userResponseDto, response);

        verify(userService, times(1)).findByEmail(anyString());
        verify(userMapper, times(1)).toDto(any(User.class));
    }

    @Test
    void testUpdateRole() {
        User user = new User("test@example.com", "John", "Doe", User.Role.CUSTOMER);
        user.setId(1L);

        UserResponseDto userResponseDto = new UserResponseDto(1L, "test@example.com", "John", "Doe", User.Role.CUSTOMER);

        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.update(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto response = userController.updateRole(1L, User.Role.MANAGER.toString());

        assertEquals(userResponseDto, response);

        verify(userService, times(1)).findById(anyLong());
        verify(userService, times(1)).update(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
    }
}