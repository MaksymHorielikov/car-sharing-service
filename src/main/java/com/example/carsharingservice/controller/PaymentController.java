package com.example.carsharingservice.controller;

import com.example.carsharingservice.config.StripeConfig;
import com.example.carsharingservice.dto.mapper.DtoMapper;
import com.example.carsharingservice.dto.request.PaymentRequestDto;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final StripeConfig stripeConfig;
    private final PaymentService paymentService;
    private final DtoMapper<Payment, PaymentRequestDto, PaymentResponseDto> paymentMapper;

    @PostMapping("/{rentalId}")
    @Operation(summary = "Create a new payment session")
    public PaymentResponseDto createPaymentSession(
            @Parameter(description = "Payment's id to create new payment session")
            @PathVariable Long rentalId) {
        return paymentMapper.toDto(paymentService.save(rentalId));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get payment by user id")
    public List<PaymentResponseDto> getPaymentsUserById(
            @Parameter(description = "Payment's id to find it")
            @PathVariable Long userId) {
        return paymentService.findByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/complete/{paymentId}")
    @Operation(summary = "Complete payment by payment id")
    public PaymentResponseDto complete(@Parameter(description = "Payment's id to complete it")
                                           @PathVariable Long paymentId) {
        Payment payment = paymentService.findById(paymentId);
        if (paymentService.checkPaymentStatus(payment.getSessionId()).equals("complete")) {
            payment.setStatus(Payment.Status.PAID);
            return paymentMapper.toDto(paymentService.update(payment));
        }
        return paymentMapper.toDto(payment);
    }

    @PostMapping("/{paymentId}/renew")
    @Operation(summary = "Renew payment by payment id")
    public PaymentResponseDto renewPayment(@Parameter(description = "Payment's id to renew it")
                                               @PathVariable Long paymentId) {
        Payment payment = paymentService.renewPayment(paymentId);
        return paymentMapper.toDto(payment);
    }

    @PostMapping("/webhook")
    @Operation(summary = "Accept webhooks")
    public ResponseEntity handleStripeWebhook(@RequestBody String payload,
                                              @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getEndpointSecret());
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }
        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElseThrow(
                            () -> new RuntimeException("Unable to get session"));
                paymentService.paymentConfirmation(session);
                break;
            case "checkout.session.expired":
                Session expiredSession = (Session) event.getDataObjectDeserializer()
                        .getObject().orElseThrow(
                            () -> new RuntimeException("Unable to get session"));
                paymentService.sessionExpired(expiredSession);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
