package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.actualReturnDate IS NULL AND r.returnDate < CURDATE() "
            + "AND r.deleted = FALSE")
    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
