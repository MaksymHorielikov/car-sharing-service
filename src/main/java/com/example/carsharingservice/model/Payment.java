package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Long rentalId;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amount;

    public Payment() {
    }

    public Payment(Long id,
                   Status status,
                   Type type,
                   Long rentalId,
                   String sessionUrl,
                   String sessionId,
                   BigDecimal amount) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.rentalId = rentalId;
        this.sessionUrl = sessionUrl;
        this.sessionId = sessionId;
        this.amount = amount;
    }

    public enum Status {
        PENDING("PENDING"), PAID("PAID"), EXPIRED("EXPIRED");
        private final String value;
        Status(String value) {
            this.value = value;
        }
    }

    public enum Type {
        PAYMENT("PAYMENT"),
        FINE("FINE");
        private final String value;

        Type(String value) {
            this.value = value;
        }
    }
}
