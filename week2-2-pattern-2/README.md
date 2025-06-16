# Server Alert System using Observer Pattern (Java)

This project implements an internal alert system using the Observer Pattern to notify different channels (email, SMS, push notifications) when server events occur.

## Why Observer Pattern?

The Observer Pattern is particularly well-suited for this alert system because:

1. **Loose Coupling**: The server (subject) doesn't need to know about the specific notification channels (observers). It just notifies all registered observers when an event occurs.

2. **Extensibility**: New notification channels can be added without modifying the existing server code. We just need to create a new observer that implements the notification interface.

3. **Dynamic Subscription**: Notification channels can be added or removed at runtime without affecting the server's operation.

4. **Separation of Concerns**: The server focuses on monitoring and detecting events, while each notification channel handles its specific delivery mechanism.

## System Components

1. **Subject (ServerMonitor)**: Monitors server metrics and notifies observers when events occur
2. **Observer Interface**: Defines the contract for all notification channels
3. **Concrete Observers**:
   - EmailNotifier
   - SMSNotifier
   - PushNotifier

## Project Structure

```
.
├── README.md
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── alertsystem/
    │               ├── model/
    │               │   └── ServerEvent.java
    │               ├── observer/
    │               │   ├── Observer.java
    │               │   └── ServerMonitor.java
    │               └── notifiers/
    │                   ├── EmailNotifier.java
    │                   ├── SMSNotifier.java
    │                   └── PushNotifier.java
    └── test/
        └── java/
            └── com/
                └── alertsystem/
                    └── AlertSystemTest.java
```

## Usage Example

```java
// Create the server monitor
ServerMonitor monitor = new ServerMonitor();

// Create and register notification channels
Observer emailNotifier = new EmailNotifier("admin@company.com");
Observer smsNotifier = new SMSNotifier("+1234567890");
Observer pushNotifier = new PushNotifier("device-token-123");

// Register observers
monitor.registerObserver(emailNotifier);
monitor.registerObserver(smsNotifier);
monitor.registerObserver(pushNotifier);

// When a server event occurs, all registered observers will be notified
ServerEvent event = new ServerEvent("CPU_OVERLOAD", "CPU usage at 95%");
monitor.notifyObservers(event);
```

## Building and Testing

```bash
# Build the project
mvn clean install

# Run tests
mvn test
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
- JUnit 5 for testing 