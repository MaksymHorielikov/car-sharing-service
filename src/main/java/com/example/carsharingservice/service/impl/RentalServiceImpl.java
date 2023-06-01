package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.RentalRepository;
import com.example.carsharingservice.service.RentalService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;

    @Override
    public Rental save(Rental rental) {
        return rentalRepository.save(rental);
    }

    @Override
    public Rental getById(Long id) {
        return rentalRepository.getReferenceById(id);
    }

    @Override
    public void delete(Long id) {
        rentalRepository.deleteById(id);
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @Override
    public List<Rental> findAllByUserId(Long userId, PageRequest pageRequest) {
        return rentalRepository.findAllByUserId(userId, pageRequest);
    }

    @Override
    public void updateActualReturnDate(Long id) {
        Rental rental = getById(id);
        if (rental.getActualReturnDate() != null) {
            rental.setActualReturnDate(LocalDateTime.now());
            save(rental);
        } else {
            throw new RuntimeException("Car is already returned ");
        }
      
    public List<Rental> findAllByActualReturnDateAfterReturnDate() {
        return rentalRepository.findAllByActualReturnDateAfterReturnDate();
    }
}
