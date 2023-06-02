package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Rental;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByUserId(Long id);

    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);

    @Query("SELECT r.rentalDate FROM Rental r WHERE r.id = :id")
    Optional<LocalDateTime> findRentalDateById(@Param("id") Long id);

    @Query("SELECT r.returnDate FROM Rental r WHERE r.id = :id")
    Optional<LocalDateTime> findReturnDateById(@Param("id") Long id);

    @Query("SELECT r.actualReturnDate FROM Rental r WHERE r.id = :id")
    Optional<LocalDateTime> findActualReturnDateById(@Param("id") Long id);
  
    @Query("SELECT r FROM Rental r WHERE r.actualReturnDate IS NULL AND r.returnDate < CURDATE() "
            + "AND r.deleted = FALSE")
    List<Rental> findAllByActualReturnDateAfterReturnDate();
}
