package com.example.carsharingservice.controller;

import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inject")
public class InjectController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Inject user with the MANAGER role")
    public String injectData() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setFirstName("admin");
        user.setLastName("admin");
        user.setPassword("$2a$10$w7V/0xSoWdkREN7ZMRuJ9.fpBkXdTmGmVa0GyxpS5fEtc4vpdYt2i");
        user.setRole(User.Role.MANAGER);
        userService.save(user);
        return "Inject was success!";
    }
}
