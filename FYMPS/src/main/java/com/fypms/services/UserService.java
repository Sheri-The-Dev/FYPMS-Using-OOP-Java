package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class UserService {
    
    public ResultSet getAllUsers() {
        try {
            String query = "SELECT * FROM users ORDER BY userID DESC";
            Statement st = DatabaseConnection.getInstance().createStatement();
            return st.executeQuery(query);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getUsersByType(String userType) {
        try {
            String query = "SELECT * FROM users WHERE userType = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setString(1, userType);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateUser(int userID, String name, String email, String department) {
        try {
            String query = "UPDATE users SET name = ?, email = ?, department = ? WHERE userID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, department);
            pst.setInt(4, userID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(int userID) {
        try {
            String query = "DELETE FROM users WHERE userID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
