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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "cars")
@SQLDelete(sql = "UPDATE cars SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Integer inventory;
    private BigDecimal dailyFee;
    private boolean deleted;

    public Car() {
    }

    public Car(String brand,
               String model,
               Type type,
               Integer inventory,
               BigDecimal dailyFee
    ) {
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.inventory = inventory;
        this.dailyFee = dailyFee;
    }

    public Car(Long id,
               String brand,
               String model,
               Type type,
               Integer inventory,
               BigDecimal dailyFee
    ) {
        this(brand, model, type, inventory, dailyFee);
        this.id = id;
    }

    public enum Type {
        SEDAN("SEDAN"),
        SUV("SUV"),
        HATCHBACK("HATCHBACK"),
        UNIVERSAL("UNIVERSAL");

        Type(String value) {
        }
    }
}
