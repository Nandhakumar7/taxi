package com.payment.strategy;

import java.math.BigDecimal;

/**
 * Interface defining the contract for payment processing strategies
 */
public interface PaymentStrategy {
    /**
     * Process the payment using the specific strategy
     *
     * @param paymentDetails PaymentDetails object containing payment information
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(PaymentDetails paymentDetails);

    /**
     * Calculate the processing fee for the payment
     *
     * @param amount The payment amount
     * @return The processing fee amount
     */
    BigDecimal getProcessingFee(BigDecimal amount);
} 