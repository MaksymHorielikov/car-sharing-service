package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Payment;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findBySessionId(String sessionId);

    Payment findByRentalId(Long id);

    @Query(value = "SELECT c.daily_fee FROM cars c JOIN rentals r ON r.car_id = c.id WHERE r.id=?1",
            nativeQuery = true)
    BigDecimal getDailyFeeByRentalId(Long id);

    @Query(value = "SELECT p.id, p.status, p.type, p.rental_id, p.amount FROM payments p JOIN rentals r ON r.id = p.rental_id WHERE r.user_id=?1",
            nativeQuery = true)
    List<Payment> findAllByUserId(Long userId);
}
