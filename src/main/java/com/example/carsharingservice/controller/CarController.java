package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.CarRequestDto;
import com.example.carsharingservice.dto.response.CarResponseDto;
import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.service.CarService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/cars")
public class CarController {
    private CarService carService;
    private final DtoMapper<Car,CarRequestDto, CarResponseDto> mapper;

    @PostMapping()
    public CarResponseDto create(@RequestBody CarRequestDto carRequestDto) {
        return mapper.toDto(carService.createCar(mapper.toModel(carRequestDto)));
    }

    @GetMapping()
    public List<CarResponseDto> getAll() {
        return carService.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarResponseDto getById(@PathVariable Long id) {
        return mapper.toDto(carService.findById(id));
    }

    @PutMapping("/{id}")
    public CarResponseDto update(@PathVariable Long id, @RequestBody CarRequestDto carRequestDto) {
        Car car = mapper.toModel(carRequestDto);
        car.setId(id);
        return mapper.toDto(carService.update(car));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        carService.deleteById(id);
    }
}