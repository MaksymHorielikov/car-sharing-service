package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private boolean deleted;

    public Rental() {
    }

    public Rental(LocalDateTime rentalDate,
                  LocalDateTime returnDate,
                  LocalDateTime actualReturnDate,
                  Car car,
                  User user) {
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.actualReturnDate = actualReturnDate;
        this.car = car;
        this.user = user;
    }

    public Rental(Long id,
                  LocalDateTime rentalDate,
                  LocalDateTime returnDate,
                  LocalDateTime actualReturnDate,
                  Car car,
                  User user) {
        this(rentalDate, returnDate, actualReturnDate, car, user);
        this.id = id;
    }
}
