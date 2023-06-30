package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.model.User;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.RentalRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

class RentalServiceImplTest {
    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Rental rental = createRental();
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental savedRental = rentalService.save(rental);

        assertNotNull(savedRental);
        assertEquals(rental, savedRental);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Rental rental = createRental();
        when(rentalRepository.getReferenceById(id)).thenReturn(rental);

        Rental retrievedRental = rentalService.getById(id);

        assertNotNull(retrievedRental);
        assertEquals(rental, retrievedRental);
        verify(rentalRepository, times(1)).getReferenceById(id);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        doNothing().when(rentalRepository).deleteById(id);

        assertDoesNotThrow(() -> rentalService.delete(id));
        verify(rentalRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindAllByUserId() {
        Long userId = 1L;
        List<Rental> rentals = Collections.singletonList(createRental());
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(rentalRepository.findAllByUserId(userId, pageRequest)).thenReturn(rentals);

        List<Rental> retrievedRentals = rentalService.findAllByUserId(userId, pageRequest);

        assertNotNull(retrievedRentals);
        assertEquals(rentals, retrievedRentals);
        verify(rentalRepository, times(1)).findAllByUserId(userId, pageRequest);
    }

    @Test
    void testUpdateActualReturnDate_RentalWithNullActualReturnDate() {
        Long rentalId = 1L;
        Rental rental = createRental();
        rental.setActualReturnDate(null);
        when(rentalRepository.getReferenceById(rentalId)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);

        assertDoesNotThrow(() -> rentalService.updateActualReturnDate(rentalId));
        assertNotNull(rental.getActualReturnDate());
        verify(rentalRepository, times(1)).getReferenceById(rentalId);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void testUpdateActualReturnDate_RentalWithNonNullActualReturnDate() {
        Long rentalId = 1L;
        Rental rental = createRental();
        rental.setActualReturnDate(LocalDateTime.now());
        when(rentalRepository.getReferenceById(rentalId)).thenReturn(rental);

        assertThrows(RuntimeException.class, () -> rentalService.updateActualReturnDate(rentalId));
        verify(rentalRepository, times(1)).getReferenceById(rentalId);
    }

    @Test
    void testFindAllByActualReturnDateAfterReturnDate_ReturnsListOfRentals() {
        List<Rental> rentals = Collections.singletonList(createRental());
        when(rentalRepository.findAllByActualReturnDateAfterReturnDate()).thenReturn(rentals);

        List<Rental> retrievedRentals = rentalService.findAllByActualReturnDateAfterReturnDate();

        assertNotNull(retrievedRentals);
        assertEquals(rentals, retrievedRentals);
        verify(rentalRepository, times(1)).findAllByActualReturnDateAfterReturnDate();
    }

    private Rental createRental() {
        LocalDateTime rentalDate = LocalDateTime.of(2023, 6, 1, 10, 0);
        LocalDateTime returnDate = LocalDateTime.of(2023, 6, 5, 12, 0);
        LocalDateTime actualReturnDate = LocalDateTime.of(2023, 6, 5, 14, 0);
        Car car = new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password", User.Role.CUSTOMER);
        return new Rental(rentalDate, returnDate, actualReturnDate, car, user);
    }
}