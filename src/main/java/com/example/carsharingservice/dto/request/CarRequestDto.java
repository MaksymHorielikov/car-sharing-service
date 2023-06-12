package com.example.carsharingservice.dto.request;

import com.example.carsharingservice.model.Car;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CarRequestDto {
    private String model;
    private String brand;
    private Car.Type type;
    private Integer inventory;
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
