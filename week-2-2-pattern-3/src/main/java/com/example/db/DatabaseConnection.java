package com.example.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface defining the contract for database connections.
 * This is the product interface in the Factory Pattern.
 */
public interface DatabaseConnection {
    /**
     * Establishes a connection to the database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the database connection
     * @throws SQLException if closing fails
     */
    void close() throws SQLException;

    /**
     * Gets the type of database connection
     * @return String representing the database type
     */
    String getDatabaseType();
} 