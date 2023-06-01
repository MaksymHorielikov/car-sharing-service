package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.repository.RentalRepository;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.RentalService;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final BigDecimal FINE_MULTIPLIER = BigDecimal.valueOf(1.5);
    private final StripePaymentService stripePaymentService;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;
    private final RentalService rentalService;

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
    public void handleSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            Rental rental = rentalRepository.findById(payment.getRentalId()).orElseThrow(() ->
                    new RuntimeException("Can't find rental by payment id: "
                            + payment.getRentalId()));
            if (rental != null) {
                payment.setStatus(Payment.Status.PAID);
                rentalRepository.save(rental);
                notificationService.sendMessage("509114006","Payment was successful: \n"
                        + payment);
            }
        }
    }

    @Override
    public void handleCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus(Payment.Status.PENDING);
            paymentRepository.save(payment);
            notificationService.sendMessage("509114006",
                    "Payment was unsuccessful: \n" + payment);
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

    @Override
    public List<Payment> findByUserId(Long userId) {
        List<Payment> payments = new ArrayList<>();
        List<Long> rentalIds = rentalService.findAllByUserId(userId).stream()
                .map(Rental::getId)
                .toList();
        for (Long id : rentalIds) {
            payments.add(paymentRepository.findByRentalId(id));
        }
        return payments;
    }

    @Override
    public List<Payment> findByUserIdAndStatus(Long userId, Payment.Status status) {
        return findByUserId(userId).stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateRentalAmount(Rental rental, Payment.Type paymentType) {
        long daysBetween = Duration.between(rental.getRentalDate(),
                rental.getReturnDate()).toDays();
        BigDecimal rentalAmount = paymentRepository.getDailyFeeByRentalId(rental.getId())
                .multiply(BigDecimal.valueOf(daysBetween));
        if (paymentType == Payment.Type.FINE
                && rental.getActualReturnDate().isAfter(rental.getReturnDate())) {
            long overdueDays = Duration.between(rental.getReturnDate(),
                    rental.getActualReturnDate()).toDays();
            BigDecimal fineAmount = paymentRepository.getDailyFeeByRentalId(rental.getId())
                    .multiply(FINE_MULTIPLIER)
                    .multiply(BigDecimal.valueOf(overdueDays));
            rentalAmount = rentalAmount.add(fineAmount);
        }
        return rentalAmount;
    }
}
