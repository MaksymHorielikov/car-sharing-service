package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Payment;
import com.stripe.model.checkout.Session;

public interface StripePaymentService {
    Session createSession(Payment payment);

    boolean checkPaymentStatus(String sessionId);
}
