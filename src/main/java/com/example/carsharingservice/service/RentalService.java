package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Rental;
import java.time.LocalDateTime;
import java.util.List;

public interface RentalService {
    Rental save(Rental rental);

    Rental getById(Long id);

    void delete(Long id);

    List<Rental> findAll();

    LocalDateTime getRentalDate(Long rentalId);

    LocalDateTime getReturnDate(Long rentalId);

    LocalDateTime getActualReturnDate(Long rentalId);
}
