package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Payment;
import com.stripe.model.checkout.Session;
import java.util.List;

public interface PaymentService {
    Payment save(Payment payment);

    Payment getPaymentById(Long id);

    Payment getPaymentByRentalId(Long id);

    Payment findBySessionId(String sessionId);

    List<Payment> getAll();

    Session createStripePaymentSession(Payment payment);
}
