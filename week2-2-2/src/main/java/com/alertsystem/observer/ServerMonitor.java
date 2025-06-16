package com.alertsystem.observer;

import com.alertsystem.model.ServerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Subject class that monitors server metrics and notifies observers of events.
 */
public class ServerMonitor {
    private static final Logger LOGGER = Logger.getLogger(ServerMonitor.class.getName());
    private final ConcurrentMap<String, Observer> observers;

    public ServerMonitor() {
        this.observers = new ConcurrentHashMap<>();
    }

    /**
     * Register a new observer to receive notifications.
     *
     * @param observer the observer to register
     * @throws IllegalArgumentException if an observer with the same identifier is already registered
     */
    public void registerObserver(Observer observer) {
        String id = observer.getIdentifier();
        if (observers.putIfAbsent(id, observer) != null) {
            throw new IllegalArgumentException("Observer with ID " + id + " is already registered");
        }
        LOGGER.info("Registered observer: " + id);
    }

    /**
     * Unregister an observer from receiving notifications.
     *
     * @param observer the observer to unregister
     */
    public void unregisterObserver(Observer observer) {
        String id = observer.getIdentifier();
        if (observers.remove(id) != null) {
            LOGGER.info("Unregistered observer: " + id);
        }
    }

    /**
     * Notify all registered observers of a server event.
     *
     * @param event the server event to notify observers about
     */
    public void notifyObservers(ServerEvent event) {
        LOGGER.info("Notifying observers of event: " + event);
        List<Exception> errors = new ArrayList<>();

        observers.forEach((id, observer) -> {
            try {
                observer.update(event);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error notifying observer " + id, e);
                errors.add(e);
            }
        });

        if (!errors.isEmpty()) {
            LOGGER.warning("Some observers failed to process the event: " + errors.size() + " errors");
        }
    }

    /**
     * Get the number of registered observers.
     *
     * @return the number of registered observers
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * Get a list of all registered observer IDs.
     *
     * @return a list of observer IDs
     */
    public List<String> getObserverIds() {
        return new ArrayList<>(observers.keySet());
    }
} 