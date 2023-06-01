package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.repository.RentalRepository;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final BigDecimal DAILY_FEE = BigDecimal.valueOf(20);
    private static final BigDecimal FINE_MULTIPLIER = BigDecimal.valueOf(1.5);
    private final StripePaymentService stripePaymentService;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    @Override
    public Payment save(Payment payment) {
        Rental rental = rentalRepository.findById(payment.getRentalId())
                .orElseThrow(() -> new RuntimeException("Can't find rental by id: "
                        + payment.getRentalId()));

        BigDecimal rentalAmount = calculateRentalAmount(rental, payment.getType());
        payment.setAmount(rentalAmount);
        Session session = stripePaymentService.createSession(payment);
        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public void handleSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            Rental rental = rentalRepository.findById(payment.getRentalId()).orElseThrow(() ->
                    new RuntimeException("Can't find rental by payment id: "
                            + payment.getRentalId()));
            if (rental != null) {
                payment.setStatus(Payment.Status.PAID);
                rentalRepository.save(rental);
                notificationService.sendMessage("Payment was successful: \n"
                        + payment.toString());
            }
        }
    }

    @Override
    public void handleCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus(Payment.Status.PENDING);
            paymentRepository.save(payment);
            notificationService.sendMessage("Payment was unsuccessful: \n" + payment.toString());
        }
    }

    @Override
    public Payment renewPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Can't find payment by id: " + paymentId));
        if (payment.getStatus() == Payment.Status.EXPIRED) {
            Session session = stripePaymentService.createSession(payment);
            payment.setSessionUrl(session.getUrl());
            payment.setSessionId(session.getId());
            payment.setStatus(Payment.Status.PENDING);
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Can't renew payment that is not expired");
        }
        return payment;
    }

    private BigDecimal calculateRentalAmount(Rental rental, Payment.Type paymentType) {
        long daysBetween = Duration.between(rental.getRentalDate(),
                rental.getReturnDate()).toDays();
        BigDecimal rentalAmount = DAILY_FEE.multiply(BigDecimal.valueOf(daysBetween));
        if (paymentType == Payment.Type.FINE
                && rental.getActualReturnDate().isAfter(rental.getReturnDate())) {
            long overdueDays = Duration.between(rental.getReturnDate(),
                    rental.getActualReturnDate()).toDays();
            BigDecimal fineAmount = DAILY_FEE.multiply(FINE_MULTIPLIER)
                    .multiply(BigDecimal.valueOf(overdueDays));
            rentalAmount = rentalAmount.add(fineAmount);
        }
        return rentalAmount;
    }
}
