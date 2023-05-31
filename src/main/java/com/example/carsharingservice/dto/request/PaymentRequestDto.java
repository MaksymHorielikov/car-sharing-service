package com.example.carsharingservice.dto.request;

import com.example.carsharingservice.model.Payment;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentRequestDto {
    private Payment.Status status;
    private Payment.Type type;
    private Long rentalId;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amount;
}
