package com.payment.strategy;

import java.math.BigDecimal;

/**
 * Record class to hold payment details
 */
public record PaymentDetails(
    BigDecimal amount,
    String sourceAccount,
    String destinationAccount,
    String currency,
    String reference
) {
    public PaymentDetails {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (sourceAccount == null || sourceAccount.isBlank()) {
            throw new IllegalArgumentException("Source account cannot be empty");
        }
        if (destinationAccount == null || destinationAccount.isBlank()) {
            throw new IllegalArgumentException("Destination account cannot be empty");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }
    }

    public PaymentDetails(BigDecimal amount, String sourceAccount, String destinationAccount) {
        this(amount, sourceAccount, destinationAccount, "USD", null);
    }
} 