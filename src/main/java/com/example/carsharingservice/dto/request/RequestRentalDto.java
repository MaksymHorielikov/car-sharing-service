package com.example.carsharingservice.dto.request;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RequestRentalDto {
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private Long carId;
    private Long userId;
}
