package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "rentals")
@SQLDelete(sql = "UPDATE rentals SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    private Long carId;
    private Long userId;
    private boolean deleted;

    public Rental() {
    }

    public Rental(LocalDateTime rentalDate,
                  LocalDateTime returnDate,
                  LocalDateTime actualReturnDate,
                  Long carId,
                  Long userId
    ) {
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.actualReturnDate = actualReturnDate;
        this.carId = carId;
        this.userId = userId;
    }

    public Rental(Long id,
                  LocalDateTime rentalDate,
                  LocalDateTime returnDate,
                  LocalDateTime actualReturnDate,
                  Long carId,
                  Long userId
    ) {
        this(rentalDate, returnDate, actualReturnDate, carId, userId);
        this.id = id;
    }
}
