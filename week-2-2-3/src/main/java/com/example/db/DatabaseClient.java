package com.example.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Example client demonstrating the usage of DatabaseConnectionFactory.
 */
public class DatabaseClient {
    public static void main(String[] args) {
        // Example connection parameters
        String mysqlUrl = "jdbc:mysql://localhost:3306/mydb";
        String postgresUrl = "jdbc:postgresql://localhost:5432/mydb";
        String username = "user";
        String password = "password";

        try {
            // Get MySQL connection
            DatabaseConnectionFactory mysqlFactory = DatabaseConnectionFactory.getFactory("mysql");
            DatabaseConnection mysqlConnection = mysqlFactory.createConnection(mysqlUrl, username, password);
            System.out.println("Created " + mysqlConnection.getDatabaseType() + " connection");

            // Get PostgreSQL connection
            DatabaseConnectionFactory postgresFactory = DatabaseConnectionFactory.getFactory("postgresql");
            DatabaseConnection postgresConnection = postgresFactory.createConnection(postgresUrl, username, password);
            System.out.println("Created " + postgresConnection.getDatabaseType() + " connection");

            // Example usage of connections
            try (Connection conn = mysqlConnection.getConnection()) {
                // Use MySQL connection
                System.out.println("MySQL connection is valid: " + conn.isValid(5));
            }

            try (Connection conn = postgresConnection.getConnection()) {
                // Use PostgreSQL connection
                System.out.println("PostgreSQL connection is valid: " + conn.isValid(5));
            }

            // Close connections
            mysqlConnection.close();
            postgresConnection.close();

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid database type: " + e.getMessage());
        }
    }
} 