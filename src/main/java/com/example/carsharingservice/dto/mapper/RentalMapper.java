package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper implements DtoMapper<Rental, RentalRequestDto, RentalResponseDto> {
    @Override
    public Rental toModel(RentalRequestDto requestDto) {
        Rental rental = new Rental();
        rental.setRentalDate(requestDto.getRentalDate());
        rental.setReturnDate(requestDto.getReturnDate());
        rental.setCarId(requestDto.getCarId());
        rental.setUserId(requestDto.getUserId());
        return rental;
    }

    @Override
    public RentalResponseDto toDto(Rental model) {
        RentalResponseDto responseRentalDto = new RentalResponseDto();
        responseRentalDto.setId(model.getId());
        responseRentalDto.setRentalDate(model.getRentalDate());
        responseRentalDto.setReturnDate(model.getReturnDate());
        responseRentalDto.setCarId(model.getCarId());
        responseRentalDto.setUserId(model.getUserId());
        return responseRentalDto;
    }
}
