package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.service.CarService;
import com.example.carsharingservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements DtoMapper<Rental, RentalRequestDto, RentalResponseDto> {
    private CarService carService;
    private UserService userService;

    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        Rental rental = new Rental();
        rental.setRentalDate(requestDto.getRentalDate());
        rental.setReturnDate(requestDto.getReturnDate());
        rental.setActualReturnDate(requestDto.getActualReturnDate());
        rental.setCar(carService.findById(requestDto.getCarId()));
        rental.setUser(userService.findById(requestDto.getUserId()));
        return rental;
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        RentalResponseDto responseRentalDto = new RentalResponseDto();
        responseRentalDto.setId(model.getId());
        responseRentalDto.setRentalDate(model.getRentalDate());
        responseRentalDto.setReturnDate(model.getReturnDate());
        responseRentalDto.setActualReturnDate(model.getActualReturnDate());
        responseRentalDto.setCarId(model.getCar().getId());
        responseRentalDto.setUserId(model.getUser().getId());
        return responseRentalDto;
    }
}
