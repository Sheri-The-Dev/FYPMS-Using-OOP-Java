package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class NotificationService {
    
    public ResultSet getNotificationsByUser(int userID) {
        try {
            String query = "SELECT * FROM notifications WHERE userID = ? ORDER BY createdAt DESC";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean createNotification(int userID, String message, String type) {
        try {
            String query = "INSERT INTO notifications (userID, message, type, isRead) VALUES (?, ?, ?, FALSE)";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            pst.setString(2, message);
            pst.setString(3, type);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markAsRead(int notificationID) {
        try {
            String query = "UPDATE notifications SET isRead = TRUE WHERE notificationID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, notificationID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}