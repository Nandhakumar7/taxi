package com.alertsystem.notifiers;

import com.alertsystem.model.ServerEvent;
import com.alertsystem.observer.Observer;

import java.util.logging.Logger;

/**
 * Concrete observer that sends SMS notifications for server events.
 */
public class SMSNotifier implements Observer {
    private static final Logger LOGGER = Logger.getLogger(SMSNotifier.class.getName());
    private final String phoneNumber;

    public SMSNotifier(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(ServerEvent event) {
        // In a real implementation, this would use an SMS service
        String message = String.format("[%s] %s: %s",
                event.getEventType(),
                event.getTimestamp(),
                event.getMessage());

        // Simulate sending SMS
        LOGGER.info(String.format("Sending SMS to %s: %s",
                phoneNumber, message));
    }

    @Override
    public String getIdentifier() {
        return "sms:" + phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
} 