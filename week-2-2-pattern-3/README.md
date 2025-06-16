# Database Connection Factory Pattern Implementation

This project demonstrates the implementation of the Factory Pattern for creating database connections. The Factory Pattern is used to create objects without explicitly specifying their exact class.

## Why Factory Pattern?

The Factory Pattern is particularly suitable for database connections for several reasons:

1. **Encapsulation**: The pattern encapsulates the complex logic of creating database connections, hiding the implementation details from the client code.

2. **Flexibility**: It allows easy addition of new database types without modifying existing code (Open/Closed Principle).

3. **Abstraction**: Clients work with the `DatabaseConnection` interface rather than concrete implementations, promoting loose coupling.

4. **Centralized Creation Logic**: All database connection creation logic is centralized in the factory classes, making it easier to maintain and modify.

5. **Runtime Polymorphism**: The factory can return different types of connections based on runtime conditions.

## Project Structure

```
src/main/java/com/example/db/
├── DatabaseConnection.java           # Interface defining connection contract
├── DatabaseConnectionFactory.java    # Abstract factory class
├── MySQLConnection.java             # MySQL connection implementation
├── MySQLConnectionFactory.java      # MySQL factory implementation
├── PostgreSQLConnection.java        # PostgreSQL connection implementation
├── PostgreSQLConnectionFactory.java # PostgreSQL factory implementation
└── DatabaseClient.java             # Example client usage
```

## Key Components

1. **DatabaseConnection Interface**: Defines the contract for all database connections
   - `getConnection()`: Establishes a connection
   - `close()`: Closes the connection
   - `getDatabaseType()`: Returns the type of database

2. **DatabaseConnectionFactory**: Abstract factory class
   - `createConnection()`: Abstract method for creating connections
   - `getFactory()`: Static factory method to get appropriate factory instance

3. **Concrete Implementations**:
   - MySQL and PostgreSQL specific connection classes
   - Corresponding factory classes for each database type

## Usage Example

```java
// Get MySQL factory
DatabaseConnectionFactory mysqlFactory = DatabaseConnectionFactory.getFactory("mysql");

// Create MySQL connection
DatabaseConnection mysqlConnection = mysqlFactory.createConnection(
    "jdbc:mysql://localhost:3306/mydb",
    "user",
    "password"
);

// Use the connection
try (Connection conn = mysqlConnection.getConnection()) {
    // Use the connection
} catch (SQLException e) {
    // Handle exception
} finally {
    mysqlConnection.close();
}
```

## Testing

The project includes unit tests to verify:
- Factory creation for different database types
- Connection creation
- Error handling for invalid database types
- Connection type verification

Run tests using:
```bash
mvn test
```

## Adding New Database Types

To add support for a new database type:

1. Create a new class implementing `DatabaseConnection`
2. Create a new factory class extending `DatabaseConnectionFactory`
3. Add the new database type to the `getFactory()` method in `DatabaseConnectionFactory`

## Dependencies

- MySQL Connector/J
- PostgreSQL JDBC Driver
- JUnit Jupiter for testing

## Building the Project

```bash
mvn clean install
``` 