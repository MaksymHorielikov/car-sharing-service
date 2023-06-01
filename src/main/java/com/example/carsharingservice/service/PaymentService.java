package com.example.carsharingservice.service;

import com.example.carsharingservice.model.Payment;
import java.util.List;

public interface PaymentService {
    Payment save(Payment payment);

    List<Payment> getUserPayments(Long userId);

    void handleSuccess(String sessionId);

    void handleCancel(String sessionId);

    Payment renewPayment(Long paymentId);
}
