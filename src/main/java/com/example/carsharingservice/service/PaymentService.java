package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Payment;
import com.stripe.model.checkout.Session;
import java.util.List;

public interface PaymentService {
    Payment save(Long rentalId);

    Payment findById(Long id);

    Payment update(Payment payment);

    void handleSuccess(String sessionId);

    void handleCancel(String sessionId);

    Payment renewPayment(Long paymentId);

    List<Payment> findByUserId(Long userId);

    String checkPaymentStatus(String sessionId);

    void paymentConfirmation(Session session);

    void sessionExpired(Session session);
}
