package com.example.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionFactoryTest {

    @Test
    void testGetMySQLFactory() {
        DatabaseConnectionFactory factory = DatabaseConnectionFactory.getFactory("mysql");
        assertNotNull(factory);
        assertTrue(factory instanceof MySQLConnectionFactory);
    }

    @Test
    void testGetPostgreSQLFactory() {
        DatabaseConnectionFactory factory = DatabaseConnectionFactory.getFactory("postgresql");
        assertNotNull(factory);
        assertTrue(factory instanceof PostgreSQLConnectionFactory);
    }

    @Test
    void testInvalidDatabaseType() {
        assertThrows(IllegalArgumentException.class, () -> {
            DatabaseConnectionFactory.getFactory("invalid");
        });
    }

    @Test
    void testCreateMySQLConnection() {
        DatabaseConnectionFactory factory = DatabaseConnectionFactory.getFactory("mysql");
        DatabaseConnection connection = factory.createConnection(
            "jdbc:mysql://localhost:3306/test",
            "user",
            "password"
        );
        assertNotNull(connection);
        assertEquals("MySQL", connection.getDatabaseType());
    }

    @Test
    void testCreatePostgreSQLConnection() {
        DatabaseConnectionFactory factory = DatabaseConnectionFactory.getFactory("postgresql");
        DatabaseConnection connection = factory.createConnection(
            "jdbc:postgresql://localhost:5432/test",
            "user",
            "password"
        );
        assertNotNull(connection);
        assertEquals("PostgreSQL", connection.getDatabaseType());
    }
} 