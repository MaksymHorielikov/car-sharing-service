package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.PaymentRequestDto;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper implements DtoMapper<Payment, PaymentRequestDto, PaymentResponseDto> {
    private final RentalService rentalService;

    @Override
    public Payment toModel(PaymentRequestDto requestDto) {
        Payment payment = new Payment();
        payment.setType(requestDto.getType());
        payment.setRental(rentalService.getById(requestDto.getRentalId()));
        payment.setAmount(requestDto.getAmount());
        return payment;
    }

    @Override
    public PaymentResponseDto toDto(Payment model) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(paymentResponseDto.getId());
        paymentResponseDto.setStatus(model.getStatus());
        paymentResponseDto.setType(model.getType());
        paymentResponseDto.setRentalId(model.getRental().getId());
        paymentResponseDto.setSessionUrl(model.getSessionUrl());
        paymentResponseDto.setSessionId(model.getSessionId());
        paymentResponseDto.setAmount(model.getAmount());
        return paymentResponseDto;
    }
}
