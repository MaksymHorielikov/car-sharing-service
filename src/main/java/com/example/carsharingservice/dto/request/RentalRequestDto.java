package com.example.carsharingservice.dto.request;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    @Min(1)
    private Long carId;
    @Min(1)
    private Long userId;
}
