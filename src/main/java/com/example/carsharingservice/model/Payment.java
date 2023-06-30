package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "payments")
@SQLDelete(sql = "UPDATE payments SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id")
    private Rental rental;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amount;
    private boolean deleted;

    public Payment() {
    }

    public Payment(Long id,
                   Status status,
                   Type type,
                   Rental rental,
                   String sessionUrl,
                   String sessionId,
                   BigDecimal amount) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.rental = rental;
        this.sessionUrl = sessionUrl;
        this.sessionId = sessionId;
        this.amount = amount;
    }

    public enum Status {
        PENDING,
        PAID,
        EXPIRED
    }

    public enum Type {
        PAYMENT,
        FINE
    }
}
