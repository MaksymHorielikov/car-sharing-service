package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.model.checkout.Session;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StripePaymentService stripePaymentService;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    @Override
    public Payment getPaymentByRentalId(Long id) {
        return paymentRepository.findByRentalId(id);
    }

    @Override
    public Payment findBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId);
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Session createStripePaymentSession(Payment payment) {
        Session session = stripePaymentService.createPaymentSession(payment);
        Payment updatedPayment = this.getPaymentById(payment.getId());
        updatedPayment.setSessionId(session.getId());
        this.save(updatedPayment);
        return session;
    }
}
