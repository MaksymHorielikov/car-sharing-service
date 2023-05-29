package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String model;
    @NotNull
    private String brand;
    private Type type;
    @Min(value = 1)
    private Integer inventory;
    @Min(value = 0)
    private BigDecimal dailyFee;

    public enum Type {
        SEDAN, SUV, HATCHBACK, UNIVERSAL
    }
}
