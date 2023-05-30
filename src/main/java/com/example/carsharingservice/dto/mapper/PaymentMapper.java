package com.example.carsharingservice.dto.mapper;

import com.example.carsharingservice.dto.request.PaymentRequestDto;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper implements DtoMapper<Payment, PaymentRequestDto, PaymentResponseDto> {
    @Override
    public Payment toModel(PaymentRequestDto requestDto) {
        Payment payment = new Payment();
        payment.setStatus(requestDto.getStatus());
        payment.setType(requestDto.getType());
        payment.setRentalId(requestDto.getRentalId());
        payment.setSessionUrl(requestDto.getSessionUrl());
        payment.setSessionId(requestDto.getSessionId());
        payment.setAmount(requestDto.getAmount());
        return payment;
    }

    @Override
    public PaymentResponseDto toDto(Payment model) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(paymentResponseDto.getId());
        paymentResponseDto.setStatus(model.getStatus());
        paymentResponseDto.setType(model.getType());
        paymentResponseDto.setRentalId(model.getRentalId());
        paymentResponseDto.setSessionUrl(model.getSessionUrl());
        paymentResponseDto.setSessionId(model.getSessionId());
        paymentResponseDto.setAmount(model.getAmount());
        return paymentResponseDto;
    }
}
