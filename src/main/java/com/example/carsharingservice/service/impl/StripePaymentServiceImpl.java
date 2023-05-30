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

@AllArgsConstructor
@Service
public class StripePaymentServiceImpl implements StripePaymentService {
    private final StripeConfig stripeConfig = new StripeConfig();

    public Session createPaymentSession(Payment payment) {
        Stripe.apiKey = stripeConfig.getSecretKey();
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Rental: " + payment.getRentalId())
                        .build();
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(payment.getAmount().longValue() * 100) // convert to cents
                        .setProductData(productData)
                        .build();
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .setCancelUrl(stripeConfig.getCancelUrl())
                .addLineItem(lineItem)
                .build();
        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Sorry, can't create payment session", e);
        }
    }

    public boolean checkPaymentStatus(String sessionId) {
        Stripe.apiKey = stripeConfig.getSecretKey();
        try {
            Session session = Session.retrieve(sessionId);
            return "paid".equals(session.getPaymentStatus());
        } catch (StripeException e) {
            throw new RuntimeException("Can't retrieve Stripe session",e);
        }
    }
}
