package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Rental;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface RentalService {
    Rental save(Rental rental);

    Rental getById(Long id);

    void delete(Long id);

    List<Rental> findAll();

    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);

    void updateActualReturnDate(Long id);

    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
