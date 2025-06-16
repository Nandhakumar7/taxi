package com.alertsystem.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a server event that triggers notifications.
 */
public class ServerEvent {
    private final String eventType;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, Object> metadata;

    public ServerEvent(String eventType, String message) {
        this(eventType, message, new HashMap<>());
    }

    public ServerEvent(String eventType, String message, Map<String, Object> metadata) {
        this.eventType = eventType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.metadata = new HashMap<>(metadata);
    }

    public String getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    @Override
    public String toString() {
        return String.format("ServerEvent{eventType='%s', message='%s', timestamp=%s}",
                eventType, message, timestamp);
    }
} 