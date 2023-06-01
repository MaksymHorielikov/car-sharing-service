package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.PaymentMapper;
import com.example.carsharingservice.dto.request.PaymentRequestDto;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.PaymentService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public PaymentResponseDto createPaymentSession(@RequestBody
                                                       PaymentRequestDto createPaymentSessionDto) {
        Payment payment = paymentMapper.toModel(createPaymentSessionDto);
        Payment createdPayment = paymentService.save(payment);
        return paymentMapper.toDto(createdPayment);
    }

    @GetMapping
    public List<PaymentResponseDto> getPaymentsByUser(@RequestParam("user_id") Long userId) {
        List<Payment> payments = paymentService.getUserPayments(userId);
        return payments.stream().map(paymentMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/success")
    public String handleSuccess(@RequestParam("sessionId") String sessionId) {
        paymentService.handleSuccess(sessionId);
        return "Payment successful!";
    }

    @GetMapping("/cancel")
    public String handleCancel(@RequestParam("sessionId") String sessionId) {
        paymentService.handleCancel(sessionId);
        return "Payment was cancelled";
    }

    @PostMapping("/{paymentId}/renew")
    public PaymentResponseDto renewPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.renewPayment(paymentId);
        return paymentMapper.toDto(payment);
    }
}
