package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.model.User;
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

import java.math.BigDecimal;
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
    void testFindById_ExistingId() {
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
    void testFindById() {
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
    void testUpdate_ExistingPayment() {
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
    void testUpdate_NonexistentPayment() {
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
    void testHandleSuccess_InvalidSessionId() {
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
    void testHandleSuccess_ExistingPayment_NoRental() {
        String sessionId = "abc123";
        Long rentalId = 1L;
        Payment payment = new Payment();
        payment.setSessionId(sessionId);

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
        verifyNoMoreInteractions(paymentRepository, rentalRepository, stripePaymentService, notificationService);
    }

    @Test
    void testHandleCancel_InvalidSessionId() {
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
    void testHandleSuccess_ExistingSessionId_ValidRentalId() {
        String sessionId = "session_123";
        Long rentalId = 1L;
        Car car = new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password", User.Role.CUSTOMER);
        Rental rental = new Rental(rentalId, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), car, user);

        Payment payment = new Payment();
        payment.setRental(rental);

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
