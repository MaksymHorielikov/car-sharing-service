package com.example.carsharingservice.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.model.User;
import com.example.carsharingservice.service.CarService;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.RentalService;
import com.example.carsharingservice.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class RentalControllerTest {
    private MockMvc mockMvc;

    @Mock
    private RentalService rentalService;

    @Mock
    private NotificationService telegramService;

    @Mock
    private CarService carService;

    @Mock
    private DtoMapper<Rental, RentalRequestDto, RentalResponseDto> rentalMapper;

    @Mock
    private UserService userService;

    private RentalController rentalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rentalController = new RentalController(
                rentalService, telegramService, carService, rentalMapper, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    public void testGetById() throws Exception {
        Long id = 1L;
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        Rental rental = new Rental();
        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        when(authentication.getName()).thenReturn("username");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(rentalService.getById(id)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/rentals/{id}", id)
                        .principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rentalResponseDto.getId()));

        verify(userService, times(1)).findByEmail(anyString());
        verify(rentalService, times(1)).getById(id);
        verify(rentalMapper, times(1)).toDto(rental);
    }

    @Test
    public void testSetActualDate() throws Exception {
        Long id = 1L;
        Rental rental = new Rental();
        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        when(rentalService.getById(id)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(rentalResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/rentals/{id}/return", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rentalResponseDto.getId()));

        verify(rentalService, times(1)).getById(id);
        verify(rentalService, times(1)).updateActualReturnDate(id);
        verify(rentalMapper, times(1)).toDto(rental);
    }
}
