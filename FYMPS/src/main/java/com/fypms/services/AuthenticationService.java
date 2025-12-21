package com.fypms.services;

import java.sql.*;
import com.fypms.database.DatabaseConnection;

public class AuthenticationService {
    
    /**
     * Authenticates a user with username, password, and user type
     * @param username The username
     * @param password The password
     * @param userType The expected user type (Student, Supervisor, Administrator, CommitteeMember)
     * @return ResultSet containing user information if authenticated, null otherwise
     */
    public ResultSet authenticate(String username, String password, String userType) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getInstance();
            
            if (conn == null) {
                System.err.println("❌ Database connection is null!");
                return null;
            }
            
            // SQL query to authenticate user with userType validation
            String sql;
            if (userType != null && !userType.isEmpty()) {
                // If userType is provided, validate it matches
                sql = "SELECT userID, username, name, userType, email " +
                      "FROM Users " +
                      "WHERE username = ? AND password = ? AND userType = ?";
                
                pstmt = conn.prepareStatement(sql, 
                                             ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                             ResultSet.CONCUR_READ_ONLY);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, userType);
            } else {
                // If userType is null, just validate username and password
                sql = "SELECT userID, username, name, userType, email " +
                      "FROM Users " +
                      "WHERE username = ? AND password = ?";
                
                pstmt = conn.prepareStatement(sql, 
                                             ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                             ResultSet.CONCUR_READ_ONLY);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
            }
            
            rs = pstmt.executeQuery();
            
            // If user found, return the ResultSet
            if (rs.next()) {
                rs.beforeFirst(); // Reset cursor to before first row
                System.out.println("✅ User authenticated: " + username);
                return rs;
            }
            
            // No user found
            System.out.println("❌ Authentication failed for: " + username + 
                              (userType != null ? " (as " + userType + ")" : ""));
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        // Note: Don't close resources here as ResultSet is being returned
    }
    
    /**
     * Validates if a username exists in the database
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                DatabaseConnection.closeResources(pstmt, rs);
                return exists;
            }
            
            DatabaseConnection.closeResources(pstmt, rs);
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get all users of a specific type
     */
    public ResultSet getUsersByType(String userType) {
        String sql = "SELECT userID, username, name, email FROM Users WHERE userType = ? ORDER BY name";
        
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                                                           ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, userType);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting users by type: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Changes user password
     */
    public boolean changePassword(int userID, String oldPassword, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE userID = ? AND password = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userID);
            pstmt.setString(3, oldPassword);
            
            int rowsAffected = pstmt.executeUpdate();
            DatabaseConnection.closeResources(pstmt, null);
            
            if (rowsAffected > 0) {
                System.out.println("✅ Password changed for userID: " + userID);
                return true;
            } else {
                System.out.println("❌ Password change failed - wrong old password");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error changing password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get user details by userID
     */
    public ResultSet getUserById(int userID) {
        String sql = "SELECT userID, username, name, userType, email FROM Users WHERE userID = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                                                           ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                           ResultSet.CONCUR_READ_ONLY);
            pstmt.setInt(1, userID);
            return pstmt.executeQuery();
            
        } catch (SQLException e) {
            System.err.println("❌ Error getting user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}