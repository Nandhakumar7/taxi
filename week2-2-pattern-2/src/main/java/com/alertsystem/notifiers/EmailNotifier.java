package com.alertsystem.notifiers;

import com.alertsystem.model.ServerEvent;
import com.alertsystem.observer.Observer;

import java.util.logging.Logger;

/**
 * Concrete observer that sends email notifications for server events.
 */
public class EmailNotifier implements Observer {
    private static final Logger LOGGER = Logger.getLogger(EmailNotifier.class.getName());
    private final String emailAddress;

    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public void update(ServerEvent event) {
        // In a real implementation, this would use an email service
        String subject = "Server Alert: " + event.getEventType();
        String body = String.format("""
                Server Event Alert
                -----------------
                Type: %s
                Message: %s
                Time: %s
                """,
                event.getEventType(),
                event.getMessage(),
                event.getTimestamp());

        // Simulate sending email
        LOGGER.info(String.format("Sending email to %s%nSubject: %s%nBody:%n%s",
                emailAddress, subject, body));
    }

    @Override
    public String getIdentifier() {
        return "email:" + emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
} 