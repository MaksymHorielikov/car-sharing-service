package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Payment;
import com.stripe.model.checkout.Session;

public interface StripePaymentService {
    Session createPaymentSession(Payment payment);

    boolean checkPaymentStatus(String sessionId);
}
