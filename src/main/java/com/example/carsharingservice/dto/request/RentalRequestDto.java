package com.example.carsharingservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RentalRequestDto {
    @NotEmpty(message = "Can`t be empty")
    @Schema(example = "2023-06-05T12:34:56")
    private LocalDateTime rentalDate;
    @NotEmpty(message = "Can`t be empty")
    @Schema(example = "2023-06-06T12:34:56")
    private LocalDateTime returnDate;
    private LocalDateTime actualReturnDate;
    @Min(1)
    @Schema(example = "1")
    @NotEmpty(message = "Can`t be empty")
    private Long carId;
    @Min(1)
    @Schema(example = "1")
    @NotEmpty(message = "Can`t be empty")
    private Long userId;
}
