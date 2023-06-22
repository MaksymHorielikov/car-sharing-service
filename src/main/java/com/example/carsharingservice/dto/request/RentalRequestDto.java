package com.example.carsharingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    @NotBlank(message = "cannot blank")
    private LocalDateTime rentalDate;
    @NotBlank(message = "cannot blank")
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    @NotBlank(message = "cannot blank")
    private Long carId;
    @NotBlank(message = "cannot blank")
    private Long userId;
}
