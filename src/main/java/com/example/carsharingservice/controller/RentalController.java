package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.service.CarService;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.RentalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.example.carsharingservice.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final NotificationService telegramService;
    private final CarService carService;
    private final UserService userService;
    private final DtoMapper<Rental, RentalRequestDto, RentalResponseDto> rentalMapper;

    @PostMapping
    @ApiOperation(value = "Create a new rental")
    public RentalResponseDto add(@RequestBody @Valid RentalRequestDto rentalDto) {

        RentalResponseDto rentalResponseDto =
                rentalMapper.toDto(rentalService.save(rentalMapper.toModel(rentalDto)));
        carService.decreaseInventory(rentalResponseDto.getCarId(), 1);
        telegramService.sendMessageAboutNewRental(responseRentalDto);       
        return rentalResponseDto;
    }

    @GetMapping
    @ApiOperation(value = "Get all rentals by userId and status")
    public List<RentalResponseDto> getByUserAndActive(
            @RequestParam("user_id")
                @ApiParam(defaultValue = "user id") Long userId,
            @RequestParam("is_active")
                @ApiParam(defaultValue = "active rental or not") boolean isActive,
            @RequestParam(defaultValue = "10")
                @ApiParam(defaultValue = "default value is '10'") Integer count,
            @RequestParam(defaultValue = "0")
                @ApiParam(defaultValue = "default value is '0'") Integer page) {
        PageRequest pageRequest = PageRequest.of(page, count);
        if (isActive) {
            return rentalService.findAllByUserId(userId, pageRequest)
                    .stream()
                    .map(rentalMapper::toDto)
                    .filter(d -> d.getReturnDate() != null)
                    .collect(Collectors.toList());
        }

        return rentalService.findAllByUserId(userId, pageRequest)
                .stream()
                .map(rentalMapper::toDto)
                .filter(d -> d.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get by id")
    public RentalResponseDto getById(@PathVariable Long id) {
        return rentalMapper.toDto(rentalService.getById(id));
    }

    @PostMapping("/{id}/return")
    @ApiOperation(value = "Set the date of car return")
    public RentalResponseDto setActualDate(@PathVariable Long id) {
        RentalResponseDto rentalResponseDto = rentalMapper.toDto(rentalService.getById(id));

        rentalService.updateActualReturnDate(id);

        carService.increaseInventory(rentalResponseDto.getCarId(), 1);

        return rentalResponseDto;
    }
}
