package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class DocumentService {
    
    public ResultSet getDocumentsByStudent(int userID) {
        try {
            String query = "SELECT d.* " +
                          "FROM documents d " +
                          "WHERE d.uploadedBy = ? " +
                          "ORDER BY d.uploadDate DESC";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getDocumentsByProject(int projectID) {
        try {
            String query = "SELECT * FROM documents WHERE projectID = ? ORDER BY uploadDate DESC";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean uploadDocument(int projectID, String title, String filePath, 
                                  String fileType, int uploadedBy) {
        try {
            String query = "INSERT INTO documents (projectID, title, filePath, fileType, uploadDate, uploadedBy) " +
                          "VALUES (?, ?, ?, ?, CURDATE(), ?)";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            pst.setString(2, title);
            pst.setString(3, filePath);
            pst.setString(4, fileType);
            pst.setInt(5, uploadedBy);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteDocument(int documentID) {
        try {
            String query = "DELETE FROM documents WHERE documentID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, documentID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
