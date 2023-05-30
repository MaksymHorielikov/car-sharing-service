package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Car;
import java.util.List;

public interface CarService {
    Car createCar(Car car);

    List<Car> findAll();

    Car findById(Long id);

    Car update(Car car);

    void deleteById(Long id);
}
