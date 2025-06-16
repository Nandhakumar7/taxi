package com.alertsystem;

import com.alertsystem.model.ServerEvent;
import com.alertsystem.notifiers.EmailNotifier;
import com.alertsystem.notifiers.PushNotifier;
import com.alertsystem.notifiers.SMSNotifier;
import com.alertsystem.observer.Observer;
import com.alertsystem.observer.ServerMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertSystemTest {

    private ServerMonitor monitor;

    @Mock
    private Observer mockObserver;

    @BeforeEach
    void setUp() {
        monitor = new ServerMonitor();
    }

    @Test
    void testRegisterAndUnregisterObserver() {
        // Register observer
        monitor.registerObserver(mockObserver);
        assertEquals(1, monitor.getObserverCount());
        assertTrue(monitor.getObserverIds().contains(mockObserver.getIdentifier()));

        // Unregister observer
        monitor.unregisterObserver(mockObserver);
        assertEquals(0, monitor.getObserverCount());
        assertFalse(monitor.getObserverIds().contains(mockObserver.getIdentifier()));
    }

    @Test
    void testDuplicateRegistration() {
        monitor.registerObserver(mockObserver);
        assertThrows(IllegalArgumentException.class, () -> monitor.registerObserver(mockObserver));
    }

    @Test
    void testNotifyObservers() {
        // Register observer
        monitor.registerObserver(mockObserver);

        // Create and send event
        ServerEvent event = new ServerEvent("TEST_EVENT", "Test message");
        monitor.notifyObservers(event);

        // Verify observer was notified
        verify(mockObserver, times(1)).update(event);
    }

    @Test
    void testMultipleNotifiers() {
        // Create notifiers
        EmailNotifier emailNotifier = new EmailNotifier("test@example.com");
        SMSNotifier smsNotifier = new SMSNotifier("+1234567890");
        PushNotifier pushNotifier = new PushNotifier("device-token-123");

        // Register all notifiers
        monitor.registerObserver(emailNotifier);
        monitor.registerObserver(smsNotifier);
        monitor.registerObserver(pushNotifier);

        // Verify registration
        assertEquals(3, monitor.getObserverCount());
        assertTrue(monitor.getObserverIds().contains(emailNotifier.getIdentifier()));
        assertTrue(monitor.getObserverIds().contains(smsNotifier.getIdentifier()));
        assertTrue(monitor.getObserverIds().contains(pushNotifier.getIdentifier()));

        // Create event with metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("severity", "high");
        metadata.put("server_id", "server-123");
        ServerEvent event = new ServerEvent("CPU_OVERLOAD", "CPU usage at 95%", metadata);

        // Notify all observers
        monitor.notifyObservers(event);

        // Verify all notifiers received the event
        // Note: In a real test, we would verify the actual notification delivery
        // Here we just verify the system doesn't throw exceptions
        assertDoesNotThrow(() -> monitor.notifyObservers(event));
    }

    @Test
    void testServerEventCreation() {
        // Test basic event creation
        ServerEvent event = new ServerEvent("TEST_EVENT", "Test message");
        assertEquals("TEST_EVENT", event.getEventType());
        assertEquals("Test message", event.getMessage());
        assertNotNull(event.getTimestamp());
        assertTrue(event.getMetadata().isEmpty());

        // Test event with metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        ServerEvent eventWithMetadata = new ServerEvent("TEST_EVENT", "Test message", metadata);
        assertEquals("value", eventWithMetadata.getMetadata().get("key"));
    }
} 