package com.app.login.Config;

import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripePaymentConfig {
	public StripePaymentConfig() {
		Stripe.apiKey = "Your_Stripe_SecKey";
	}
}
