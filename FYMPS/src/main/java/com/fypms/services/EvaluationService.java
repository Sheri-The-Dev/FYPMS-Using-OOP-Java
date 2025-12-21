package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class EvaluationService {
    
    public ResultSet getEvaluationsByProject(int projectID) {
        try {
            String query = "SELECT * FROM evaluations WHERE projectID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean submitEvaluation(int projectID, double technicalScore, 
                                    double documentationScore, double presentationScore, 
                                    String evaluatedBy) {
        try {
            double totalScore = (technicalScore + documentationScore + presentationScore) / 3;
            
            String query = "INSERT INTO evaluations (projectID, technicalScore, documentationScore, " +
                          "presentationScore, totalScore, evaluatedBy, evaluationDate) " +
                          "VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            pst.setDouble(2, technicalScore);
            pst.setDouble(3, documentationScore);
            pst.setDouble(4, presentationScore);
            pst.setDouble(5, totalScore);
            pst.setString(6, evaluatedBy);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}