package com.example.carsharingservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.repository.PaymentRepository;
import com.example.carsharingservice.repository.RentalRepository;
import com.example.carsharingservice.service.NotificationService;
import com.example.carsharingservice.service.PaymentService;
import com.example.carsharingservice.service.StripePaymentService;
import java.time.LocalDateTime;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentServiceImplTest {
    private final StripePaymentService stripePaymentService = Mockito.mock(StripePaymentService.class);
    private final PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private final RentalRepository rentalRepository = Mockito.mock(RentalRepository.class);
    private final NotificationService notificationService = Mockito.mock(NotificationService.class);

    @Test
    void findById_ExistingId_ReturnsPayment() {
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        Payment result = paymentService.findById(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());

        verify(paymentRepository, times(1)).findById(paymentId);
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void findById_NonexistentId_ThrowsException() {
        Long paymentId = 1L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        assertThrows(RuntimeException.class, () -> paymentService.findById(paymentId));

        verify(paymentRepository, times(1)).findById(paymentId);
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void update_ExistingPayment_UpdatesAndReturnsPayment() {
        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
        when(paymentRepository.saveAndFlush(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        Payment updatedPayment = new Payment();
        updatedPayment.setId(payment.getId());
        updatedPayment.setStatus(Payment.Status.PAID);

        Payment result = paymentService.update(updatedPayment);

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(Payment.Status.PAID, result.getStatus());

        verify(paymentRepository, times(1)).findById(payment.getId());
        verify(paymentRepository, times(1)).saveAndFlush(any(Payment.class));
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void update_NonexistentPayment_ThrowsException() {
        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.empty());

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        assertThrows(EntityNotFoundException.class, () -> paymentService.update(payment));

        verify(paymentRepository, times(1)).findById(payment.getId());
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void handleSuccess_InvalidSessionId_NoUpdatesOrNotifications() {
        String sessionId = "invalidSessionId";

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(null);

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        paymentService.handleSuccess(sessionId);

        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void handleSuccess_ExistingPayment_NoRental_ThrowsException() {
        String sessionId = "abc123";
        Long rentalId = 1L;
        Payment payment = new Payment();
        payment.setSessionId(sessionId);
        payment.setRentalId(rentalId);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        assertThrows(RuntimeException.class, () -> paymentService.handleSuccess(sessionId));

        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(rentalRepository, times(1)).findById(rentalId);
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void handleCancel_InvalidSessionId_NoUpdatesOrNotifications() {
        String sessionId = "invalidSessionId";

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(null);

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        paymentService.handleCancel(sessionId);

        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void handleSuccess_ExistingSessionId_ValidRentalId_UpdatesPaymentAndSendsNotification() {
        String sessionId = "session_123";
        Long rentalId = 1L;

        Rental rental = new Rental(rentalId, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Payment payment = new Payment();
        payment.setRentalId(rentalId);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        PaymentService paymentService = new PaymentServiceImpl(
                stripePaymentService,
                paymentRepository,
                rentalRepository,
                notificationService
        );

        paymentService.handleSuccess(sessionId);

        assertEquals(Payment.Status.PAID, payment.getStatus());

        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(rentalRepository, times(1)).findById(rentalId);
        verify(paymentRepository, times(1)).save(payment);
        verify(notificationService, times(1)).sendMessage(anyString(), anyString());
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }
}
