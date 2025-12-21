package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class ProposalService {
    
    public ResultSet getPendingProposals(int supervisorUserID) {
        try {
            Connection conn = DatabaseConnection.getInstance();
            String query = "SELECT pr.*, p.title, s.studentID " +
                          "FROM proposals pr " +
                          "JOIN projects p ON pr.projectID = p.projectID " +
                          "LEFT JOIN students s ON pr.submittedBy = s.studentID " +
                          "JOIN supervisors sup ON pr.supervisorID = sup.supervisorID " +
                          "WHERE sup.userID = ? AND pr.status = 'Pending' " +
                          "ORDER BY pr.submissionDate DESC";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, supervisorUserID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getAllProposals() {
        try {
            Connection conn = DatabaseConnection.getInstance();
            String query = "SELECT * FROM proposals ORDER BY proposalID DESC";
            Statement st = conn.createStatement();
            return st.executeQuery(query);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateProposalStatus(int proposalID, String status) {
        try {
            Connection conn = DatabaseConnection.getInstance();
            if (conn == null) {
                return false;
            }
            
            conn.setAutoCommit(false);
            
            String proposalQuery = "UPDATE proposals SET status = ? WHERE proposalID = ?";
            PreparedStatement proposalPst = conn.prepareStatement(proposalQuery);
            proposalPst.setString(1, status);
            proposalPst.setInt(2, proposalID);
            proposalPst.executeUpdate();
            
            if ("Approved".equals(status)) {
                String projectQuery = "UPDATE projects SET status = 'Approved' " +
                                     "WHERE projectID = (SELECT projectID FROM proposals WHERE proposalID = ?)";
                PreparedStatement projectPst = conn.prepareStatement(projectQuery);
                projectPst.setInt(1, proposalID);
                projectPst.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            return true;
            
        } catch (SQLException e) {
            try {
                Connection conn = DatabaseConnection.getInstance();
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean submitProposal(int projectID, String objectives, String methodology, 
                                  String supervisorID, String studentID) {
        try {
            Connection conn = DatabaseConnection.getInstance();
            String query = "INSERT INTO proposals (projectID, submissionDate, status, objectives, methodology, supervisorID, submittedBy) " +
                          "VALUES (?, CURDATE(), 'Pending', ?, ?, ?, ?)";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, projectID);
            pst.setString(2, objectives);
            pst.setString(3, methodology);
            pst.setString(4, supervisorID);
            pst.setString(5, studentID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}