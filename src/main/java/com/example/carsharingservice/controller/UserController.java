package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.UserMapper;
import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.dto.response.UserResponseDto;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public UserResponseDto get(Authentication authentication) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String email = details.getUsername();
        return userMapper.toDto(userService.findByEmail(email).get());
    }

    @PutMapping("/me")
    public UserResponseDto update(Authentication authentication, UserRequestDto userRequestDto) {
        UserDetails details = (UserDetails) authentication.getPrincipal();
        String email = details.getUsername();
        Long userId = userService.findByEmail(email).get().getId();
        User user = userMapper.toModel(userRequestDto);
        user.setId(userId);
        return userMapper.toDto(userService.update(user));
    }
}
