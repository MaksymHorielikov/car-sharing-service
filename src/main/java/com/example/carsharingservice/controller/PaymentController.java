package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.PaymentMapper;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.PaymentService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/{rentalId}")
    public PaymentResponseDto createPaymentSession(@PathVariable Long rentalId) {
        return paymentMapper.toDto(paymentService.save(rentalId));
    }

    @GetMapping("/{userId}")
    public List<PaymentResponseDto> getPaymentsUserById(@PathVariable Long userId) {
        return paymentService.findByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/complete/{paymentId}")
    public PaymentResponseDto complete(@PathVariable Long paymentId) {
        Payment payment = paymentService.findById(paymentId);
        if (paymentService.checkPaymentStatus(payment.getSessionId()).equals("complete")) {
            payment.setStatus(Payment.Status.PAID);
            return paymentMapper.toDto(paymentService.update(payment));
        }
        return paymentMapper.toDto(payment);
    }

    @PostMapping("/{paymentId}/renew")
    public PaymentResponseDto renewPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.renewPayment(paymentId);
        return paymentMapper.toDto(payment);
    }
}
