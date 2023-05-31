package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.returnDate < r.actualReturnDate " +
            "OR (r.actualReturnDate IS NULL AND r.returnDate < CURDATE()) AND r.deleted = FALSE")
    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
