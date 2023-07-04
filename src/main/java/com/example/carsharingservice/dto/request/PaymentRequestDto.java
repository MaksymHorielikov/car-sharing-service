package com.example.carsharingservice.dto.request;

import com.example.carsharingservice.model.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @Schema(example = "PAYMENT")
    @NotEmpty(message = "Can`t be empty")
    @Enumerated(EnumType.STRING)
    private Payment.Type type;
    @Schema(example = "1")
    @NotEmpty(message = "Can`t be empty")
    @Positive
    private Long rentalId;
    @Schema(example = "50")
    @NotEmpty(message = "Can`t be empty")
    @Positive
    private BigDecimal amount;
}
