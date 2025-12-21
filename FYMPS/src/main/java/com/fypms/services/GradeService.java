package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class GradeService {
    
    // FIXED: Connection type instead of DatabaseConnection
    private Connection dbConnection;
    
    public GradeService() {
        // FIXED: No casting needed - getInstance() returns Connection
        dbConnection = DatabaseConnection.getInstance();
    }
    
    public ResultSet getGradesByStudent(int userID) {
        String query = "SELECT g.*, p.title FROM grades g " +
                      "JOIN students s ON g.studentID = s.studentID " +
                      "JOIN projects p ON g.projectID = p.projectID " +
                      "WHERE s.userID = ?";
        
        try {
            // FIXED: Now dbConnection is Connection type, so prepareStatement works
            PreparedStatement pst = dbConnection.prepareStatement(query);
            pst.setInt(1, userID);
            return pst.executeQuery();
        } catch (SQLException e) {
            System.err.println("❌ Error getting grades: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all grades for a specific project
     */
    public ResultSet getGradesByProject(int projectID) {
        String query = "SELECT g.*, s.name as studentName FROM grades g " +
                      "JOIN students st ON g.studentID = st.studentID " +
                      "JOIN users s ON st.userID = s.userID " +
                      "WHERE g.projectID = ?";
        
        try {
            PreparedStatement pst = dbConnection.prepareStatement(query);
            pst.setInt(1, projectID);
            return pst.executeQuery();
        } catch (SQLException e) {
            System.err.println("❌ Error getting project grades: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Add or update grade for a student
     */
    public boolean saveGrade(int studentID, int projectID, float proposalGrade, 
                            float midtermGrade, float finalGrade, String comments) {
        float totalGrade = proposalGrade + midtermGrade + finalGrade;
        String letterGrade = calculateLetterGrade(totalGrade);
        
        String query = "INSERT INTO grades (studentID, projectID, proposalGrade, midtermGrade, " +
                      "finalGrade, totalGrade, letterGrade, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE proposalGrade = ?, midtermGrade = ?, finalGrade = ?, " +
                      "totalGrade = ?, letterGrade = ?, comments = ?";
        
        try {
            PreparedStatement pst = dbConnection.prepareStatement(query);
            pst.setInt(1, studentID);
            pst.setInt(2, projectID);
            pst.setFloat(3, proposalGrade);
            pst.setFloat(4, midtermGrade);
            pst.setFloat(5, finalGrade);
            pst.setFloat(6, totalGrade);
            pst.setString(7, letterGrade);
            pst.setString(8, comments);
            // For UPDATE part
            pst.setFloat(9, proposalGrade);
            pst.setFloat(10, midtermGrade);
            pst.setFloat(11, finalGrade);
            pst.setFloat(12, totalGrade);
            pst.setString(13, letterGrade);
            pst.setString(14, comments);
            
            int result = pst.executeUpdate();
            DatabaseConnection.closeResources(pst, null);
            
            if (result > 0) {
                System.out.println("✅ Grade saved successfully");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error saving grade: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Calculate letter grade based on total marks
     */
    private String calculateLetterGrade(float total) {
        if (total >= 90) return "A+";
        else if (total >= 85) return "A";
        else if (total >= 80) return "A-";
        else if (total >= 75) return "B+";
        else if (total >= 70) return "B";
        else if (total >= 65) return "B-";
        else if (total >= 60) return "C+";
        else if (total >= 55) return "C";
        else if (total >= 50) return "C-";
        else return "F";
    }
    
    /**
     * Get student's CGPA
     */
    public float getStudentCGPA(int userID) {
        String query = "SELECT AVG(totalGrade) as cgpa FROM grades g " +
                      "JOIN students s ON g.studentID = s.studentID " +
                      "WHERE s.userID = ?";
        
        try {
            PreparedStatement pst = dbConnection.prepareStatement(query);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                float cgpa = rs.getFloat("cgpa");
                DatabaseConnection.closeResources(pst, rs);
                return cgpa / 25.0f; // Convert to 4.0 scale
            }
            
            DatabaseConnection.closeResources(pst, rs);
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating CGPA: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0.0f;
    }
}