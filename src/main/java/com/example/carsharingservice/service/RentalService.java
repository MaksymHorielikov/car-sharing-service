package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Rental;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface RentalService {
    Rental save(Rental rental);

    Rental getById(Long id);

    void delete(Long id);

    List<Rental> findAll();

    LocalDateTime getRentalDate(Long rentalId);

    LocalDateTime getReturnDate(Long rentalId);

    LocalDateTime getActualReturnDate(Long rentalId);
  
    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);

    List<Rental> findAllByUserId(Long id);

    void updateActualReturnDate(Long id);

    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
