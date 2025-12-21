package com.fypms.database;

import java.sql.*;

/**
 * DatabaseConnection class handles all database connectivity
 * Uses Singleton pattern to ensure only one connection instance
 */
public class DatabaseConnection {
    
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smartfypms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Default XAMPP password is empty
    
    // Singleton instance
    private static Connection connection = null;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }
    
    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get singleton database connection instance
     * This method returns a Connection object, not DatabaseConnection
     */
    public static Connection getInstance() {
        try {
            // If connection is null or closed, create a new one
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("✅ Database connected successfully");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Alternative method name for clarity - same as getInstance()
     */
    public static Connection getConnection() {
        return getInstance();
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getInstance();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Close database resources safely
     */
    public static void closeResources(Statement stmt, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}