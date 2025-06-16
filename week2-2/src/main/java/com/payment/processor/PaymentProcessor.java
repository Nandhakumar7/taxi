package com.payment.processor;

import com.payment.strategy.PaymentStrategy;
import com.payment.strategy.PaymentDetails;

import java.math.BigDecimal;

/**
 * Context class that uses payment strategies to process payments.
 * This class maintains a reference to a PaymentStrategy object and
 * delegates the payment processing to the strategy object.
 */
public class PaymentProcessor {
    private PaymentStrategy strategy;

    /**
     * Initialize the payment processor with a specific strategy
     *
     * @param strategy The payment strategy to use
     */
    public PaymentProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Change the payment strategy at runtime
     *
     * @param strategy The new payment strategy to use
     */
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Process a payment using the current strategy
     *
     * @param amount The payment amount
     * @param sourceAccount The source account number
     * @param destinationAccount The destination account number
     * @param currency The currency code
     * @param reference Optional reference number for the transfer
     * @return true if payment was successful, false otherwise
     */
    public boolean processPayment(
            BigDecimal amount,
            String sourceAccount,
            String destinationAccount,
            String currency,
            String reference) {
        
        PaymentDetails paymentDetails = new PaymentDetails(
            amount,
            sourceAccount,
            destinationAccount,
            currency,
            reference
        );

        // Calculate and display the processing fee
        BigDecimal fee = strategy.getProcessingFee(amount);
        System.out.printf("Processing fee: %s %s%n", fee, currency);

        // Process the payment using the current strategy
        return strategy.processPayment(paymentDetails);
    }

    /**
     * Process a payment using the current strategy with default currency (USD)
     *
     * @param amount The payment amount
     * @param sourceAccount The source account number
     * @param destinationAccount The destination account number
     * @return true if payment was successful, false otherwise
     */
    public boolean processPayment(
            BigDecimal amount,
            String sourceAccount,
            String destinationAccount) {
        
        return processPayment(amount, sourceAccount, destinationAccount, "USD", null);
    }

    /**
     * Get the processing fee for a given amount using the current strategy
     *
     * @param amount The payment amount
     * @return The processing fee amount
     */
    public BigDecimal getProcessingFee(BigDecimal amount) {
        return strategy.getProcessingFee(amount);
    }
} 