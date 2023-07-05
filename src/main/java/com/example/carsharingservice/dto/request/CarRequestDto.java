package com.example.carsharingservice.dto.request;

import com.example.carsharingservice.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarRequestDto {
    @Schema(example = "Camry")
    @NotEmpty(message = "Can`t be empty")
    private String model;
    @Schema(example = "Toyota")
    @NotEmpty(message = "Can`t be empty")
    private String brand;
    @Schema(example = "SEDAN")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Car.Type type;
    @Schema(example = "10")
    @NotNull
    @PositiveOrZero
    private Integer inventory;
    @Schema(example = "50")
    @NotNull
    @Positive
    private BigDecimal dailyFee;

    public CarRequestDto(String model,
                         String brand,
                         Car.Type type,
                         Integer inventory,
                         BigDecimal dailyFee) {
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.inventory = inventory;
        this.dailyFee = dailyFee;
    }
}
