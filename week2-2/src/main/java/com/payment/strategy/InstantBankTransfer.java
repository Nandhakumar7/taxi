package com.payment.strategy;

import java.math.BigDecimal;

/**
 * Strategy implementation for instant bank transfers (e.g., Zelle, Venmo)
 */
public class InstantBankTransfer implements PaymentStrategy {
    private static final BigDecimal FIXED_FEE = new BigDecimal("1.00");

    @Override
    public boolean processPayment(PaymentDetails paymentDetails) {
        // Simulate instant transfer processing
        System.out.printf("Processing instant transfer of %s %s%n", 
            paymentDetails.amount(), paymentDetails.currency());
        System.out.printf("From: %s%n", paymentDetails.sourceAccount());
        System.out.printf("To: %s%n", paymentDetails.destinationAccount());
        // In real implementation, this would integrate with instant payment networks
        return true;
    }

    @Override
    public BigDecimal getProcessingFee(BigDecimal amount) {
        return FIXED_FEE; // Fixed fee regardless of amount
    }
} 