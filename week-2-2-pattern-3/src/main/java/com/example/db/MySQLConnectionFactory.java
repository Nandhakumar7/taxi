package com.example.db;

/**
 * Concrete factory implementation for creating MySQL connections.
 */
public class MySQLConnectionFactory extends DatabaseConnectionFactory {
    @Override
    public DatabaseConnection createConnection(String url, String username, String password) {
        return new MySQLConnection(url, username, password);
    }
} 