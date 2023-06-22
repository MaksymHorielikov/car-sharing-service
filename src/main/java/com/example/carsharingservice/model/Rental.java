package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private Car car;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private boolean deleted;
}
