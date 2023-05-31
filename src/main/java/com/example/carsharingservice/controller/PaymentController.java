package com.example.carsharingservice.controller;

import com.example.carsharingservice.dto.mapper.PaymentMapper;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.StripePaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final StripePaymentService stripePaymentService;
    private final PaymentMapper paymentMapper;

    // Get payments for a specific user
    @GetMapping
    public PaymentResponseDto getPaymentsUserById(@RequestParam("rental_id") Long rentalId) {
        return paymentMapper.toDto(paymentService.getPaymentByRentalId(rentalId));
    }

    // Create a new payment session
    @PostMapping
    public void createPaymentSession(@RequestBody Long rentalId) {
        Payment payment = paymentService.getPaymentByRentalId(rentalId);
        stripePaymentService.createPaymentSession(payment);
    }

    // Check for successful payment
    @GetMapping("/success")
    public String checkPaymentSuccess(@RequestParam("session_id") String sessionId) {
        Payment payment = paymentService.findBySessionId(sessionId);

        if (payment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        if (payment.getStatus() == Payment.Status.PAID) {
            return "Payment successful";
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Payment not successful");
        }
    }

    // Handle cancelled payment
    @GetMapping("/cancel")
    public String handleCancelledPayment() {
        return "Payment unsuccessful";
    }
}
