package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.RequestRentalDto;
import com.example.carsharingservice.dto.response.ResponseRentalDto;
import com.example.carsharingservice.model.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper implements DtoMapper<Rental, RequestRentalDto, ResponseRentalDto> {
    @Override
    public Rental toModel(RequestRentalDto requestDto) {
        Rental rental = new Rental();
        rental.setRentalDate(requestDto.getRentalDate());
        rental.setReturnDate(requestDto.getReturnDate());
        rental.setCarId(requestDto.getCarId());
        rental.setUserId(requestDto.getUserId());
        return rental;
    }

    @Override
    public ResponseRentalDto toDto(Rental model) {
        ResponseRentalDto responseRentalDto = new ResponseRentalDto();
        responseRentalDto.setId(model.getId());
        responseRentalDto.setRentalDate(model.getRentalDate());
        responseRentalDto.setReturnDate(model.getReturnDate());
        responseRentalDto.setCarId(model.getCarId());
        responseRentalDto.setUserId(model.getUserId());
        return responseRentalDto;
    }
}
