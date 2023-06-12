package com.example.carsharingservice.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.repository.CarRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCar() {
        Car car = new Car("Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));
        when(carRepository.save(car)).thenReturn(car);
        Car result = carService.createCar(car);
        assertEquals(car, result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void testFindAll() {
        List<Car> cars = Arrays.asList(
                new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, BigDecimal.valueOf(50)),
                new Car(2L, "Honda", "Accord", Car.Type.SEDAN, 8, BigDecimal.valueOf(60))
        );
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carService.findAll();
        assertEquals(cars, result);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Long carId = 1L;
        Car car = new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, BigDecimal.valueOf(50));
        when(carRepository.getReferenceById(carId)).thenReturn(car);
        Car result = carService.findById(carId);
        assertEquals(car, result);
        verify(carRepository, times(1)).getReferenceById(carId);
    }

    @Test
    public void testUpdate() {
        Long carId = 1L;
        Car car = new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, BigDecimal.valueOf(50));
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(car)).thenReturn(car);
        Car result = carService.update(car);
        assertEquals(car, result);
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).saveAndFlush(car);
    }

    @Test
    public void testDeleteById() {
        Long carId = 1L;
        carService.deleteById(carId);
        verify(carRepository, times(1)).deleteById(carId);
    }
}