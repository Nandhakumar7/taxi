package com.alertsystem.observer;

import com.alertsystem.model.ServerEvent;

/**
 * Interface for all notification observers in the alert system.
 * Implementations of this interface will handle different types of notifications
 * (email, SMS, push notifications, etc.).
 */
public interface Observer {
    /**
     * Called by the subject when a server event occurs.
     *
     * @param event the server event that triggered the notification
     */
    void update(ServerEvent event);

    /**
     * Returns a unique identifier for this observer.
     *
     * @return a unique identifier string
     */
    String getIdentifier();
} 