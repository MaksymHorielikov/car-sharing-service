package com.example.carsharingservice.repository;

import com.example.carsharingservice.model.Rental;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByUserId(Long userId, PageRequest pageRequest);
}
