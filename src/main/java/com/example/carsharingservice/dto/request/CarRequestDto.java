package com.example.carsharingservice.dto.request;

import com.example.carsharingservice.model.Car;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarRequestDto {
    @NotEmpty(message = "This field can`t be empty")
    private String model;
    @NotEmpty(message = "This field can`t be empty")
    private String brand;
    @NotEmpty(message = "This field can`t be empty")
    private Car.Type type;
    @NotEmpty(message = "This field can`t be empty")
    @PositiveOrZero
    private Integer inventory;
    @NotEmpty(message = "This field can`t be empty")
    private BigDecimal dailyFee;

    public CarRequestDto(String model,
                         String brand,
                         Car.Type type,
                         Integer inventory,
                         BigDecimal dailyFee
    ) {
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.inventory = inventory;
        this.dailyFee = dailyFee;
    }
}
