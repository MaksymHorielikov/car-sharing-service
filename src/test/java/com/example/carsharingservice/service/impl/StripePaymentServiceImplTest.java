package com.example.carsharingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.config.StripeConfig;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.Stripe;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StripePaymentServiceImplTest {
    private StripePaymentService stripePaymentService;

    @Mock
    private StripeConfig stripeConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stripePaymentService = new StripePaymentServiceImpl(stripeConfig);
    }

    @Test
    void createSession_StripeException_ThrowsRuntimeException() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(50));

        when(stripeConfig.getSuccessUrl()).thenReturn("/payments/success");
        when(stripeConfig.getCancelUrl()).thenReturn("/payments/cancel");
        when(stripeConfig.getSecretKey()).thenReturn("test_secret_key");

        Stripe.apiKey = "test_secret_key";

        assertThrows(RuntimeException.class, () -> stripePaymentService.createSession(payment));

        verify(stripeConfig, times(1)).getSuccessUrl();
        verify(stripeConfig, times(1)).getCancelUrl();
    }
}