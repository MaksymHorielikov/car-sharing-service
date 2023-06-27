package com.example.carsharingservice.controller;

import static org.mockito.ArgumentMatchers.any;

import com.example.carsharingservice.security.AuthenticationService;
import com.example.carsharingservice.security.jwt.JwtTokenProvider;
import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.exception.AuthenticationException;
import com.example.carsharingservice.model.User;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void testRegister() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", "password", User.Role.CUSTOMER);
        UserRequestDto userRequestDto = new UserRequestDto("test@example.com", "John", "Doe", "password");
        Mockito.when(authenticationService.register(userRequestDto)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"password\":\"password\",\"role\":\"CUSTOMER\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", "password", User.Role.CUSTOMER);
        Mockito.when(authenticationService.login(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(jwtTokenProvider.createToken(user.getEmail(), Collections.singletonList(user.getRole().name())))
                .thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        Mockito.when(authenticationService.login(any(String.class), any(String.class)))
                .thenThrow(new AuthenticationException("Login With Invalid Credentials"));
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid username or password!"));
    }
}