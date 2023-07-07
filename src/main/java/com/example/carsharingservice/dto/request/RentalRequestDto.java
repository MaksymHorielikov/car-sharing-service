package com.example.carsharingservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    @Schema(example = "2023-06-05T12:34:56")
    private LocalDateTime rentalDate;
    @Schema(example = "2023-06-06T12:34:56")
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    @Min(1)
    @Schema(example = "1")
    private Long carId;
    @Min(1)
    @Schema(example = "1")
    private Long userId;
}
