package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final DtoMapper<Rental, RentalRequestDto, RentalResponseDto> rentalMapper;

    @PostMapping
    public RentalResponseDto add(@RequestBody @Valid RentalRequestDto rentalDto) {
        RentalResponseDto responseRentalDto =
                rentalMapper.toDto(rentalService.save(rentalMapper.toModel(rentalDto)));
        // decrease car inventory by 1;
        return responseRentalDto;
    }

    @GetMapping("/{user_id}/{is_active}")
    public RentalResponseDto getByUserAndActive(@PathVariable Long userId,
                                                @PathVariable boolean isActive) {
        // need user service
        return null;
    }

    @GetMapping("/{id}")
    public RentalResponseDto getById(@PathVariable Long id) {
        return rentalMapper.toDto(rentalService.getById(id));
    }

    @PostMapping("/{id}/return")
    public RentalResponseDto setActualDate(@RequestParam Long id) {
        // need car service
        return null;
    }
}
