package com.example.db;

/**
 * Abstract factory class for creating database connections.
 * This is the creator in the Factory Pattern.
 */
public abstract class DatabaseConnectionFactory {
    /**
     * Creates a new database connection
     * @param url Database connection URL
     * @param username Database username
     * @param password Database password
     * @return DatabaseConnection instance
     */
    public abstract DatabaseConnection createConnection(String url, String username, String password);

    /**
     * Factory method to get the appropriate factory instance
     * @param dbType Type of database (e.g., "mysql", "postgresql")
     * @return DatabaseConnectionFactory instance
     * @throws IllegalArgumentException if database type is not supported
     */
    public static DatabaseConnectionFactory getFactory(String dbType) {
        return switch (dbType.toLowerCase()) {
            case "mysql" -> new MySQLConnectionFactory();
            case "postgresql" -> new PostgreSQLConnectionFactory();
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        };
    }
} 