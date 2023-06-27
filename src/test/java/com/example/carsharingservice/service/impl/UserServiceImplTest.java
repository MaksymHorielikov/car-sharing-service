package com.example.carsharingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.model.User;
import com.example.carsharingservice.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ValidUser_ReturnsSavedUserWithEncodedPassword() {
        User user = createUser();
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void findById_ExistingId_ReturnsUser() {
        Long id = 1L;
        User user = createUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User retrievedUser = userService.findById(id);

        assertNotNull(retrievedUser);
        assertEquals(user, retrievedUser);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void findById_NonExistingId_ThrowsException() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsOptionalUser() {
        String email = "test@example.com";
        User user = createUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.findByEmail(email);

        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsEmptyOptional() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userService.findByEmail(email);

        assertFalse(retrievedUser.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void delete_ExistingId_DeletesUser() {
        Long id = 1L;
        doNothing().when(userRepository).deleteById(id);

        assertDoesNotThrow(() -> userService.delete(id));
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void findAll_ReturnsListOfUsers() {
        List<User> users = Collections.singletonList(createUser());
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.findAll();

        assertNotNull(retrievedUsers);
        assertEquals(users, retrievedUsers);
        verify(userRepository, times(1)).findAll();
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setRole(User.Role.CUSTOMER);
        user.setChatId("chat123");
        return user;
    }
}