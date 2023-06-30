package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.RentalService;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final BigDecimal FINE_MULTIPLIER = BigDecimal.valueOf(1.5);
    private final StripePaymentService stripePaymentService;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final RentalService rentalService;

    @Override
    public Payment save(Long rentalId) {
        Rental rental = rentalService.getById(rentalId);
        Payment payment = new Payment();
        payment.setRental(rentalService.getById(rentalId));
        payment.setType(checkPaymentType(rental));
        BigDecimal rentalAmount = calculateRentalAmount(rental, payment.getType());
        payment.setAmount(rentalAmount);
        Session session = stripePaymentService.createSession(payment);
        payment.setStatus(Payment.Status.PENDING);
        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Can't find payment by id: " + id));
    }

    @Override
    public Payment update(Payment payment) {
        paymentRepository.findById(payment.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find payment with id " + payment.getId()));
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public void handleSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId);
        if (payment != null) {
            Rental rental = rentalService.getById(payment.getRental().getId());
            if (rental != null) {
                payment.setStatus(Payment.Status.PAID);
                paymentRepository.save(payment);
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
        return paymentRepository.findAllByUserId(userId);
    }

    @Override
    public String checkPaymentStatus(String sessionId) {
        return stripePaymentService.checkPaymentStatus(sessionId);
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

    private Payment.Type checkPaymentType(Rental rental) {
        long expectedDaysBetween = Duration.between(rental.getRentalDate(),
                rental.getReturnDate()).toDays();
        long daysBetween = Duration.between(rental.getRentalDate(),
                rental.getActualReturnDate()).toDays();
        if (expectedDaysBetween >= daysBetween) {
            return Payment.Type.PAYMENT;
        }
        return Payment.Type.FINE;
    }

    @Override
    public void paymentConfirmation(Session session) {
        String sessionId = session.getId();
        Payment bySessionId = paymentRepository.findBySessionId(sessionId);
        bySessionId.setStatus(Payment.Status.PAID);
        update(bySessionId);
    }

    @Override
    public void sessionExpired(Session session) {
        String sessionId = session.getId();
        Payment bySessionId = paymentRepository.findBySessionId(sessionId);
        bySessionId.setStatus(Payment.Status.EXPIRED);
        update(bySessionId);
    }
}
