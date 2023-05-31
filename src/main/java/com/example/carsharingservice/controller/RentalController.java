package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.RentalRequestDto;
import com.example.carsharingservice.dto.response.RentalResponseDto;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.service.CarService;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.RentalService;
import com.example.carsharingservice.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
    public RentalResponseDto add(@RequestBody @Valid RentalRequestDto rentalDto) {
        RentalResponseDto responseRentalDto =
                rentalMapper.toDto(rentalService.save(rentalMapper.toModel(rentalDto)));
        // decrease car inventory by 1;
        telegramService.sendMessage(userService.findById(responseRentalDto.getUserId()).getChatId(),
                "New rental was added with ID: "
                        + responseRentalDto.getId() + "\n"
                        + "Car brand:" + carService.findById(responseRentalDto.getCarId())
                        .getBrand() + "\n"
                        + "Rental date: " + responseRentalDto.getRentalDate() + "\n"
                        + "Return date: " + responseRentalDto.getReturnDate());
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

    @Scheduled(cron = "*/10 * * * * *")
    public void notifyAllUsersWhereActualReturnDateIsAfterReturnDate() {
        List<Rental> rentals = rentalService.findAllByActualReturnDateAfterReturnDate();
        for (Rental rental : rentals) {
            telegramService.sendMessage(userService.findById(rental.getUserId()).getChatId(),
                    "Your car has to be "
                            + "return, because your rental ended ");
        }
    }
}
