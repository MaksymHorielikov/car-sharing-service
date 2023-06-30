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

    @Query(value = "SELECT c.daily_fee FROM cars c JOIN rentals r ON r.car_id = c.id WHERE r.id=?1 "
            + "AND c.deleted = FALSE", nativeQuery = true)
    BigDecimal getDailyFeeByRentalId(Long id);

    @Query(value = "FROM Payment p "
            + "LEFT JOIN FETCH p.rental pr "
            + "LEFT JOIN FETCH pr.car "
            + "LEFT JOIN FETCH pr.user pru "
            + "WHERE pru.id =: userId AND p.deleted = FALSE")
    List<Payment> findAllByUserId(Long userId);
}
