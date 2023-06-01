package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.UserMapper;
import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.dto.response.UserResponseDto;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public UserResponseDto get(Authentication authentication) {
        return userMapper.toDto(userService.findByEmail(authentication.getName()).get());
    }

    @PutMapping("/me")
    public UserResponseDto update(Authentication authentication,
                                  @RequestBody UserRequestDto userRequestDto) {
        Long userId = userService.findByEmail(authentication.getName()).get().getId();
        User user = userMapper.toModel(userRequestDto);
        user.setId(userId);
        user.setRole(userService.findById(userId).getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userService.update(user));
    }

    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id, @RequestParam String role) {
        try {
            User user = userService.findById(id);
            user.setRole(User.Role.valueOf(role));
            return userMapper.toDto(userService.update(user));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid user role " + role, e);
        }
    }
}
