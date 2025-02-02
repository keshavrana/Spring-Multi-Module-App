package com.app.login.Config;

import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripePaymentConfig {
	public StripePaymentConfig() {
		Stripe.apiKey = "sk_test_7ZHMOSp9nzcyJd4AS5ribCQX00JIq28utO";
	}
}
