package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Payment;
import java.math.BigDecimal;
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
}
