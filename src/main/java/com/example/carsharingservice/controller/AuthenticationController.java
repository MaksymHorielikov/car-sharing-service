package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.UserMapper;
import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.dto.response.UserResponseDto;
import com.example.carsharingservice.exception.AuthenticationException;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.security.AuthenticationService;
import com.example.carsharingservice.security.jwt.JwtTokenProvider;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        User user = authenticationService.register(userRequestDto);
        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserRequestDto userRequestDto) {
        User user;
        try {
            user = authenticationService
                    .login(userRequestDto.getEmail(), userRequestDto.getPassword());
            String token = jwtTokenProvider.createToken(user.getEmail(),
                    List.of(user.getRole().name()));
            return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/register")
    public String getRegister(){
        return "Success";
    }
}
