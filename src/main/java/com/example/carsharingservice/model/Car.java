package com.example.carsharingservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String model;
    @NotNull
    private String brand;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Min(value = 1)
    private Integer inventory;
    @Min(value = 0)
    private BigDecimal dailyFee;
    private boolean deleted = false;

    public enum Type {
        SEDAN("Sedan"),
        SUV("Suv"),
        HATCHBACK("Hatchback"),
        UNIVERSAL("Universal");

        public final String value;

        Type(String value) {
            this.value = value;
        }
    }
}
