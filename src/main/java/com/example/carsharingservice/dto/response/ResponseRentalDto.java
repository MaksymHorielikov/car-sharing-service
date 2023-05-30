package com.example.carsharingservice.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResponseRentalDto {
    private Long id;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private Long carId;
    private Long userId;
}
