package com.example.carsharingservice.controller;

import com.example.carsharingservice.config.StripeConfig;
import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.model.Rental;
import com.example.carsharingservice.model.User;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.carsharingservice.dto.mapper.PaymentMapper;
import com.example.carsharingservice.dto.response.PaymentResponseDto;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PaymentControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentController paymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentController = new PaymentController(new StripeConfig(), paymentService, paymentMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testCreatePaymentSession() throws Exception {
        Long rentalId = 1L;
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, Payment.Status.PAID, Payment.Type.PAYMENT,
                createRental(), "https://example.com/session", "session123", new BigDecimal("100.00"));

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(payment.getId());

        when(paymentService.save(rentalId)).thenReturn(payment);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/payments/{rentalId}", rentalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(payment.getId()));
    }

    @Test
    public void testCompleteWithCompletePayment() throws Exception {
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, Payment.Status.PAID, Payment.Type.PAYMENT,
                createRental(), "https://example.com/session", "session123", new BigDecimal("100.00"));

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(payment.getId());
        paymentResponseDto.setStatus(payment.getStatus());

        when(paymentService.findById(paymentId)).thenReturn(payment);
        when(paymentService.checkPaymentStatus(payment.getSessionId())).thenReturn("complete");
        when(paymentService.update(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/payments/complete/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(payment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(payment.getStatus().toString()));
    }

    @Test
    public void testCompleteWithIncompletePayment() throws Exception {
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, Payment.Status.PAID, Payment.Type.PAYMENT,
                createRental(), "https://example.com/session", "session123", new BigDecimal("100.00"));

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(payment.getId());

        when(paymentService.findById(paymentId)).thenReturn(payment);
        when(paymentService.checkPaymentStatus(payment.getSessionId())).thenReturn("incomplete");
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/payments/complete/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(payment.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").doesNotExist());
    }

    @Test
    public void testRenewPayment() throws Exception {
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId, Payment.Status.PAID, Payment.Type.PAYMENT,
                createRental(), "https://example.com/session", "session123", new BigDecimal("100.00"));

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(payment.getId());

        when(paymentService.renewPayment(paymentId)).thenReturn(payment);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/payments/{paymentId}/renew", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(payment.getId()));
    }

    private Rental createRental() {
        LocalDateTime rentalDate = LocalDateTime.of(2023, 6, 1, 10, 0);
        LocalDateTime returnDate = LocalDateTime.of(2023, 6, 5, 12, 0);
        LocalDateTime actualReturnDate = LocalDateTime.of(2023, 6, 5, 14, 0);
        Car car = new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password", User.Role.CUSTOMER);
        return new Rental(rentalDate, returnDate, actualReturnDate, car, user);
    }
}
