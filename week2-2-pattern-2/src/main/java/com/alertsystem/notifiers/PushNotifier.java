package com.alertsystem.notifiers;

import com.alertsystem.model.ServerEvent;
import com.alertsystem.observer.Observer;

import java.util.logging.Logger;

/**
 * Concrete observer that sends push notifications for server events.
 */
public class PushNotifier implements Observer {
    private static final Logger LOGGER = Logger.getLogger(PushNotifier.class.getName());
    private final String deviceToken;

    public PushNotifier(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void update(ServerEvent event) {
        // In a real implementation, this would use a push notification service
        // (e.g., Firebase Cloud Messaging, Apple Push Notification Service)
        String title = "Server Alert: " + event.getEventType();
        String body = event.getMessage();
        
        // Simulate sending push notification
        LOGGER.info(String.format("""
                Sending push notification to device %s
                Title: %s
                Body: %s
                Metadata: %s""",
                deviceToken,
                title,
                body,
                event.getMetadata()));
    }

    @Override
    public String getIdentifier() {
        return "push:" + deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
} 