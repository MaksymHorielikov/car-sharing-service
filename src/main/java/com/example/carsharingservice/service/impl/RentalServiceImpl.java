package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.repository.RentalRepository;
import com.example.carsharingservice.service.RentalService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Rental save(Rental rental) {
        Long userId = rental.getUserId();
        List<Payment> pendingPayments = paymentRepository.findByUserIdAndStatus(userId,
                Payment.Status.PENDING);
        if (!pendingPayments.isEmpty()) {
            throw new RuntimeException("User with id: " + userId + " has pending payments.");
        }
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

    public LocalDateTime getRentalDate(Long rentalId) {
        return rentalRepository.findRentalDateById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental with id "
                        + rentalId + " not found"));
    }

    public LocalDateTime getReturnDate(Long rentalId) {
        return rentalRepository.findReturnDateById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental with id "
                        + rentalId + " not found"));
    }

    public LocalDateTime getActualReturnDate(Long rentalId) {
        return rentalRepository.findActualReturnDateById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental with id "
                        + rentalId + " not found"));
    }
}
