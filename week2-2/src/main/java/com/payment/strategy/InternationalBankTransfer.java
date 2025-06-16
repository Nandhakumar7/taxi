package com.payment.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Strategy implementation for international bank transfers (SWIFT)
 */
public class InternationalBankTransfer implements PaymentStrategy {
    private static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.005"); // 0.5%

    @Override
    public boolean processPayment(PaymentDetails paymentDetails) {
        // Simulate international bank transfer processing
        System.out.printf("Processing international SWIFT transfer of %s %s%n", 
            paymentDetails.amount(), paymentDetails.currency());
        System.out.printf("From: %s%n", paymentDetails.sourceAccount());
        System.out.printf("To: %s%n", paymentDetails.destinationAccount());
        if (paymentDetails.reference() != null) {
            System.out.printf("Reference: %s%n", paymentDetails.reference());
        }
        // In real implementation, this would integrate with SWIFT network
        return true;
    }

    @Override
    public BigDecimal getProcessingFee(BigDecimal amount) {
        return amount.multiply(FEE_PERCENTAGE)
                    .setScale(2, RoundingMode.HALF_UP);
    }
} 