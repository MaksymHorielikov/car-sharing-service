package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.CarRequestDto;
import com.example.carsharingservice.dto.response.CarResponseDto;
import com.example.carsharingservice.model.Car;
import org.springframework.stereotype.Component;

@Component
public class CarMapper implements DtoMapper<Car,CarRequestDto, CarResponseDto> {
    @Override
    public Car toModel(CarRequestDto requestDto) {
        Car car = new Car();
        car.setModel(requestDto.getModel());
        car.setBrand(requestDto.getBrand());
        car.setType(requestDto.getType());
        car.setInventory(requestDto.getInventory());
        car.setDailyFee(requestDto.getDailyFee());
        return car;
    }

    @Override
    public CarResponseDto toDto(Car model) {
        CarResponseDto carResponseDto = new CarResponseDto();
        carResponseDto.setId(model.getId());
        carResponseDto.setModel(model.getModel());
        carResponseDto.setBrand(model.getBrand());
        carResponseDto.setType(model.getType());
        carResponseDto.setInventory(model.getInventory());
        carResponseDto.setDailyFee(model.getDailyFee());
        return carResponseDto;
    }
}
