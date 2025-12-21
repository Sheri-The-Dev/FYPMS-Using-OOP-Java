/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.services;

import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class ProjectService {
    
    public ResultSet getProjectsByStudent(int userID) {
        try {
            String query = "SELECT DISTINCT p.* " +
                          "FROM projects p " +
                          "LEFT JOIN groups g ON p.groupID = g.groupID " +
                          "LEFT JOIN group_members gm ON g.groupID = gm.groupID " +
                          "LEFT JOIN students s ON gm.studentID = s.studentID " +
                          "WHERE s.userID = ? " +
                          "ORDER BY p.projectID DESC";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getProjectsBySupervisor(int userID) {
        try {
            String query = "SELECT p.* " +
                          "FROM projects p " +
                          "JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                          "WHERE s.userID = ? " +
                          "ORDER BY p.projectID DESC";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            return pst.executeQuery();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResultSet getAllProjects() {
        try {
            String query = "SELECT * FROM projects ORDER BY projectID DESC";
            Statement st = DatabaseConnection.getInstance().createStatement();
            return st.executeQuery(query);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateProjectStatus(int projectID, String status) {
        try {
            String query = "UPDATE projects SET status = ? WHERE projectID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setString(1, status);
            pst.setInt(2, projectID);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}