package com.payment;

import com.payment.processor.PaymentProcessor;
import com.payment.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStrategyTest {
    private PaymentProcessor processor;
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("1000.00");
    private static final String SOURCE_ACCOUNT = "1234567890";
    private static final String DESTINATION_ACCOUNT = "0987654321";

    @BeforeEach
    void setUp() {
        processor = new PaymentProcessor(new DomesticBankTransfer());
    }

    @Test
    void testDomesticTransferFee() {
        BigDecimal fee = processor.getProcessingFee(TEST_AMOUNT);
        assertEquals(new BigDecimal("1.00"), fee); // 0.1% of 1000
    }

    @Test
    void testInternationalTransferFee() {
        processor.setStrategy(new InternationalBankTransfer());
        BigDecimal fee = processor.getProcessingFee(TEST_AMOUNT);
        assertEquals(new BigDecimal("5.00"), fee); // 0.5% of 1000
    }

    @Test
    void testInstantTransferFee() {
        processor.setStrategy(new InstantBankTransfer());
        BigDecimal fee = processor.getProcessingFee(TEST_AMOUNT);
        assertEquals(new BigDecimal("1.00"), fee); // Fixed fee
    }

    @Test
    void testDomesticTransferProcessing() {
        boolean result = processor.processPayment(
            TEST_AMOUNT,
            SOURCE_ACCOUNT,
            DESTINATION_ACCOUNT
        );
        assertTrue(result);
    }

    @Test
    void testInternationalTransferProcessing() {
        processor.setStrategy(new InternationalBankTransfer());
        boolean result = processor.processPayment(
            TEST_AMOUNT,
            SOURCE_ACCOUNT,
            DESTINATION_ACCOUNT,
            "EUR",
            "REF123"
        );
        assertTrue(result);
    }

    @Test
    void testInstantTransferProcessing() {
        processor.setStrategy(new InstantBankTransfer());
        boolean result = processor.processPayment(
            TEST_AMOUNT,
            SOURCE_ACCOUNT,
            DESTINATION_ACCOUNT
        );
        assertTrue(result);
    }

    @Test
    void testInvalidPaymentDetails() {
        assertThrows(IllegalArgumentException.class, () ->
            new PaymentDetails(
                BigDecimal.ZERO,
                SOURCE_ACCOUNT,
                DESTINATION_ACCOUNT
            )
        );

        assertThrows(IllegalArgumentException.class, () ->
            new PaymentDetails(
                TEST_AMOUNT,
                "",
                DESTINATION_ACCOUNT
            )
        );

        assertThrows(IllegalArgumentException.class, () ->
            new PaymentDetails(
                TEST_AMOUNT,
                SOURCE_ACCOUNT,
                null
            )
        );
    }
} 