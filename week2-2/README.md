# Bank Transfer Strategy Pattern Implementation

This project demonstrates the implementation of the Strategy pattern for a bank transfer payment processing system. The Strategy pattern is used to define a family of algorithms (payment processing strategies), encapsulate each one, and make them interchangeable.

## Why Strategy Pattern Fits This Use Case

The Strategy pattern is particularly well-suited for the bank transfer system for several reasons:

### 1. Multiple Payment Processing Algorithms
- Different types of bank transfers (Domestic, International, Instant) require different processing logic
- Each transfer type has its own fee calculation rules
- The Strategy pattern allows us to encapsulate each transfer type's specific algorithm in separate classes

### 2. Runtime Flexibility
- Payment processing methods can be changed at runtime
- New payment methods can be added without modifying existing code
- The `PaymentProcessor` can switch between strategies without knowing their implementation details
- Example:
  ```java
  PaymentProcessor processor = new PaymentProcessor(new DomesticBankTransfer());
  // Later, switch to international transfer
  processor.setStrategy(new InternationalBankTransfer());
  ```

### 3. Separation of Concerns
- Each payment strategy is isolated in its own class
- Payment processing logic is separated from the client code
- Fee calculation is encapsulated within each strategy
- Makes the code more maintainable and testable

### 4. Open/Closed Principle
- The system is open for extension (new payment strategies can be added)
- The system is closed for modification (existing code doesn't need to change)
- New payment methods can be added by implementing the `PaymentStrategy` interface

### 5. Code Reusability
- Common payment processing logic is shared through the `PaymentStrategy` interface
- The `PaymentDetails` record provides a consistent data structure
- Fee calculation logic is encapsulated within each strategy
- Reduces code duplication

### 6. Type Safety and Validation
- Java's strong typing ensures only valid strategies can be used
- The `PaymentDetails` record provides immutable data with validation
- `BigDecimal` is used for precise monetary calculations
- Input validation is centralized in the `PaymentDetails` constructor

## Project Structure

```
src/main/java/com/payment/
├── strategy/
│   ├── PaymentStrategy.java        # Strategy interface
│   ├── PaymentDetails.java         # Data class for payment information
│   ├── DomesticBankTransfer.java   # Domestic transfer strategy
│   ├── InternationalBankTransfer.java  # International transfer strategy
│   └── InstantBankTransfer.java    # Instant transfer strategy
└── processor/
    └── PaymentProcessor.java       # Context class that uses strategies
```

## Key Components

1. **PaymentStrategy Interface**
   - Defines the contract for all payment strategies
   - Declares methods for payment processing and fee calculation

2. **Concrete Strategies**
   - `DomesticBankTransfer`: For domestic bank transfers (0.1% fee)
   - `InternationalBankTransfer`: For international SWIFT transfers (0.5% fee)
   - `InstantBankTransfer`: For instant transfers (fixed $1 fee)

3. **PaymentProcessor (Context)**
   - Maintains a reference to a payment strategy
   - Delegates payment processing to the current strategy
   - Allows switching between strategies at runtime

4. **PaymentDetails (Data Class)**
   - Immutable record class for payment information
   - Includes validation in the constructor
   - Provides a consistent data structure for all strategies

## Usage Example

```java
// Create a payment processor with domestic transfer strategy
PaymentProcessor processor = new PaymentProcessor(new DomesticBankTransfer());

// Process a domestic payment
processor.processPayment(
    new BigDecimal("1000.00"),
    "1234567890",
    "0987654321"
);

// Switch to international transfer
processor.setStrategy(new InternationalBankTransfer());
processor.processPayment(
    new BigDecimal("5000.00"),
    "1234567890",
    "0987654321",
    "EUR",
    "REF123"
);
```

## Testing

The project includes comprehensive unit tests that verify:
- Fee calculations for each strategy
- Payment processing functionality
- Strategy switching
- Input validation
- Error handling

Run tests using Maven:
```bash
mvn test
```

## Future Extensions

The Strategy pattern makes it easy to add new payment methods:
1. Create a new class implementing `PaymentStrategy`
2. Implement the required methods
3. Use the new strategy with the existing `PaymentProcessor`

No changes to existing code are required to add new payment methods. 