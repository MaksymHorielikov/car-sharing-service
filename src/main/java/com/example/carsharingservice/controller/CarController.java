package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.CarRequestDto;
import com.example.carsharingservice.dto.response.CarResponseDto;
import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
    @Operation(summary = "Create a new car")
    public CarResponseDto create(
            @Parameter(
                    description = "New car to add to service",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CarRequestDto.class)))
            @RequestBody @Valid CarRequestDto carRequestDto) {
        return mapper.toDto(carService.createCar(mapper.toModel(carRequestDto)));
    }

    @GetMapping()
    @Operation(summary = "Get all cars")
    public List<CarResponseDto> getAll() {
        return carService.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by id")
    public CarResponseDto getById(@Parameter(description = "Car's id to find it")
                                      @PathVariable Long id) {
        return mapper.toDto(carService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car")
    public CarResponseDto update(@Parameter(description = "Car's id to update it")
                                     @PathVariable Long id,
                                 @Parameter(
                                         description = "Car with required changes to update",
                                         required = true,
                                         content = @Content(schema = @Schema(implementation =
                                                 CarRequestDto.class)))
                                 @RequestBody @Valid CarRequestDto carRequestDto) {
        Car car = mapper.toModel(carRequestDto);
        car.setId(id);
        return mapper.toDto(carService.update(car));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car")
    public void delete(@Parameter(description = "Car's id to delete it") @PathVariable Long id) {
        carService.deleteById(id);
    }
}
