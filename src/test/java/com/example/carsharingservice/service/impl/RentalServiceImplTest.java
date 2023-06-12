package com.example.carsharingservice.service.impl;

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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    void save_ValidRental_ReturnsSavedRental() {
        Rental rental = createRental();
        when(rentalRepository.save(rental)).thenReturn(rental);

        Rental savedRental = rentalService.save(rental);

        assertNotNull(savedRental);
        assertEquals(rental, savedRental);
        verify(rentalRepository, times(1)).save(rental);
    }

    @Test
    void getById_ExistingId_ReturnsRental() {
        Long id = 1L;
        Rental rental = createRental();
        when(rentalRepository.getReferenceById(id)).thenReturn(rental);

        Rental retrievedRental = rentalService.getById(id);

        assertNotNull(retrievedRental);
        assertEquals(rental, retrievedRental);
        verify(rentalRepository, times(1)).getReferenceById(id);
    }

    @Test
    void delete_ExistingId_DeletesRental() {
        Long id = 1L;
        doNothing().when(rentalRepository).deleteById(id);

        assertDoesNotThrow(() -> rentalService.delete(id));
        verify(rentalRepository, times(1)).deleteById(id);
    }

    @Test
    void findAll_ReturnsListOfRentals() {
        List<Rental> rentals = Collections.singletonList(createRental());
        when(rentalRepository.findAll()).thenReturn(rentals);

        List<Rental> retrievedRentals = rentalService.findAll();

        assertNotNull(retrievedRentals);
        assertEquals(rentals, retrievedRentals);
        verify(rentalRepository, times(1)).findAll();
    }

    @Test
    void getRentalDate_ExistingRentalId_ReturnsRentalDate() {
        Long rentalId = 1L;
        Rental rental = createRental();
        when(rentalRepository.findRentalDateById(rentalId)).thenReturn(Optional.of(rental.getRentalDate()));

        LocalDateTime rentalDate = rentalService.getRentalDate(rentalId);

        assertNotNull(rentalDate);
        assertEquals(rental.getRentalDate(), rentalDate);
        verify(rentalRepository, times(1)).findRentalDateById(rentalId);
    }

    @Test
    void getRentalDate_NonExistingRentalId_ThrowsException() {
        Long rentalId = 1L;
        when(rentalRepository.findRentalDateById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentalService.getRentalDate(rentalId));
        verify(rentalRepository, times(1)).findRentalDateById(rentalId);
    }

    @Test
    void getReturnDate_ExistingRentalId_ReturnsReturnDate() {
        Long rentalId = 1L;
        Rental rental = createRental();
        when(rentalRepository.findReturnDateById(rentalId)).thenReturn(Optional.of(rental.getReturnDate()));

        LocalDateTime returnDate = rentalService.getReturnDate(rentalId);

        assertNotNull(returnDate);
        assertEquals(rental.getReturnDate(), returnDate);
        verify(rentalRepository, times(1)).findReturnDateById(rentalId);
    }

    @Test
    void getReturnDate_NonExistingRentalId_ThrowsException() {
        Long rentalId = 1L;
        when(rentalRepository.findReturnDateById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentalService.getReturnDate(rentalId));
        verify(rentalRepository, times(1)).findReturnDateById(rentalId);
    }

    @Test
    void getActualReturnDate_ExistingRentalId_ReturnsActualReturnDate() {
        Long rentalId = 1L;
        Rental rental = createRental();
        when(rentalRepository.findActualReturnDateById(rentalId)).thenReturn(Optional.of(rental.getActualReturnDate()));

        LocalDateTime actualReturnDate = rentalService.getActualReturnDate(rentalId);

        assertNotNull(actualReturnDate);
        assertEquals(rental.getActualReturnDate(), actualReturnDate);
        verify(rentalRepository, times(1)).findActualReturnDateById(rentalId);
    }

    @Test
    void getActualReturnDate_NonExistingRentalId_ThrowsException() {
        Long rentalId = 1L;
        when(rentalRepository.findActualReturnDateById(rentalId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rentalService.getActualReturnDate(rentalId));
        verify(rentalRepository, times(1)).findActualReturnDateById(rentalId);
    }

    @Test
    void findAllByUserId_ExistingUserId_ReturnsListOfRentals() {
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
    void updateActualReturnDate_RentalWithNullActualReturnDate_SetsActualReturnDateAndSavesRental() {
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
    void updateActualReturnDate_RentalWithNonNullActualReturnDate_ThrowsException() {
        Long rentalId = 1L;
        Rental rental = createRental();
        rental.setActualReturnDate(LocalDateTime.now());
        when(rentalRepository.getReferenceById(rentalId)).thenReturn(rental);

        assertThrows(RuntimeException.class, () -> rentalService.updateActualReturnDate(rentalId));
        verify(rentalRepository, times(1)).getReferenceById(rentalId);
    }

    @Test
    void findAllByActualReturnDateAfterReturnDate_ReturnsListOfRentals() {
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
        Long carId = 1L;
        Long userId = 1L;
        return new Rental(rentalDate, returnDate, actualReturnDate, carId, userId);
    }
}