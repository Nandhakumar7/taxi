package com.example.db;

/**
 * Concrete factory implementation for creating PostgreSQL connections.
 */
public class PostgreSQLConnectionFactory extends DatabaseConnectionFactory {
    @Override
    public DatabaseConnection createConnection(String url, String username, String password) {
        return new PostgreSQLConnection(url, username, password);
    }
} 