package com.example.carsharingservice.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StripeInitializer {
    private final StripeConfig stripeConfig;

    @Autowired
    public StripeInitializer(StripeConfig stripeConfig) {
        this.stripeConfig = stripeConfig;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeConfig.getSecretKey();
    }
}
