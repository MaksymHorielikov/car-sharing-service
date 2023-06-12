package com.example.carsharingservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.dto.mapper.UserMapper;
import com.example.carsharingservice.dto.request.UserRequestDto;
import com.example.carsharingservice.exception.AuthenticationException;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationServiceImplTest {
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(userService, passwordEncoder, userMapper);
    }

    @Test
    public void testRegister() {
        User user = new User(1L, "test@example.com", "John", "Doe", "password", User.Role.CUSTOMER);
        UserRequestDto userRequestDto = new UserRequestDto("test@example.com", "John", "Doe", "password");

        when(userMapper.toModel(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.save(user)).thenReturn(user);

        User registeredUser = authenticationService.register(userRequestDto);

        assertNotNull(registeredUser);
        assertEquals(User.Role.CUSTOMER, registeredUser.getRole());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userService, times(1)).save(user);
    }

    @Test
    public void testLoginWithValidCredentials() throws AuthenticationException {
        String login = "test@example.com";
        String password = "password";
        User user = new User(1L, "test@example.com", "John", "Doe", "password", User.Role.CUSTOMER);

        when(userService.findByEmail(login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        User loggedInUser = authenticationService.login(login, password);

        assertNotNull(loggedInUser);
        assertEquals(user, loggedInUser);
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        String login = "test@example.com";
        String password = "password";
        User user = new User(1L, "test@example.com", "John", "Doe", "password", User.Role.CUSTOMER);

        when(userService.findByEmail(login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authenticationService.login(login, password));
    }
}