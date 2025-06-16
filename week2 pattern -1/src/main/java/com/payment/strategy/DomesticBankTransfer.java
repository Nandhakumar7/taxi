package com.payment.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Strategy implementation for domestic bank transfers
 */
public class DomesticBankTransfer implements PaymentStrategy {
    private static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.001"); // 0.1%

    @Override
    public boolean processPayment(PaymentDetails paymentDetails) {
        // Simulate domestic bank transfer processing
        System.out.printf("Processing domestic transfer of %s %s%n", 
            paymentDetails.amount(), paymentDetails.currency());
        System.out.printf("From: %s%n", paymentDetails.sourceAccount());
        System.out.printf("To: %s%n", paymentDetails.destinationAccount());
        // In real implementation, this would integrate with a bank's API
        return true;
    }

    @Override
    public BigDecimal getProcessingFee(BigDecimal amount) {
        return amount.multiply(FEE_PERCENTAGE)
                    .setScale(2, RoundingMode.HALF_UP);
    }
} 