package com.example.carsharingservice.service.impl;

import com.example.carsharingservice.config.StripeConfig;
import com.example.carsharingservice.model.Payment;
import com.example.carsharingservice.service.StripePaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@Service
public class StripePaymentServiceImpl implements StripePaymentService {
    private final StripeConfig stripeConfig;

    public Session createSession(Payment payment) {
        String baseUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8082")
                .path("/payments")
                .toUriString();
//        String successUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .path(stripeConfig.getSuccessUrl())
//                .toUriString();
//        String cancelUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
//                .path(stripeConfig.getCancelUrl())
//                .toUriString();
        SessionCreateParams.LineItem.PriceData.ProductData productData
                = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Car rental")
                .build();
        SessionCreateParams.LineItem.PriceData priceData
                = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(payment.getAmount().longValue() * 100)
                .setProductData(productData)
                .build();
        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(priceData)
                .setQuantity(1L)
                .build();
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(stripeConfig.getCancelUrl())
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .addLineItem(lineItem)
                .build();

        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Can't create Stripe payment Session ", e);
        }
    }

    @Override
    public String checkPaymentStatus(String sessionId) {
        Stripe.apiKey = stripeConfig.getSecretKey();
        try {
            Session session = Session.retrieve(sessionId);
            return session.getStatus();
        } catch (StripeException e) {
            throw new RuntimeException("Can't retrieve Stripe session",e);
        }
    }
}
