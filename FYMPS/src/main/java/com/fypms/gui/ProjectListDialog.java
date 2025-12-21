/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class ProjectListDialog extends JDialog {
    
    private int userID;
    private String userType;
    private JTable projectTable;
    private DefaultTableModel tableModel;
    
    public ProjectListDialog(JFrame parent, int userID, String userType) {
        super(parent, "My Projects", true);
        this.userID = userID;
        this.userType = userType;
        initComponents();
        loadProjects();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📁 My Projects");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"Project ID", "Title", "Status", "Supervisor", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        projectTable = new JTable(tableModel);
        projectTable.setRowHeight(35);
        projectTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        projectTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        projectTable.getTableHeader().setBackground(new Color(52, 73, 94));
        projectTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(projectTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewBtn = new JButton("👁️ View Details");
        viewBtn.setPreferredSize(new Dimension(140, 40));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        viewBtn.setBorderPainted(false);
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> viewDetails());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(241, 196, 15));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadProjects());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProjects() {
        tableModel.setRowCount(0);
        
        try {
            String query = "";
            PreparedStatement pst = null;
            
            if ("Student".equals(userType)) {
                // Get projects for student
                query = "SELECT DISTINCT p.projectID, p.title, p.status, u.name as supervisor, p.finalGrade " +
                       "FROM projects p " +
                       "LEFT JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                       "LEFT JOIN users u ON s.userID = u.userID " +
                       "LEFT JOIN groups g ON p.groupID = g.groupID " +
                       "LEFT JOIN group_members gm ON g.groupID = gm.groupID " +
                       "LEFT JOIN students st ON gm.studentID = st.studentID " +
                       "WHERE st.userID = ? " +
                       "ORDER BY p.projectID DESC";
                
                pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, userID);
                
            } else if ("Supervisor".equals(userType)) {
                // Get projects supervised by this supervisor
                query = "SELECT p.projectID, p.title, p.status, u.name as supervisor, p.finalGrade " +
                       "FROM projects p " +
                       "LEFT JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                       "LEFT JOIN users u ON s.userID = u.userID " +
                       "WHERE u.userID = ? " +
                       "ORDER BY p.projectID DESC";
                
                pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, userID);
            }
            
            if (pst != null) {
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("projectID"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getString("supervisor") != null ? rs.getString("supervisor") : "N/A",
                        rs.getObject("finalGrade") != null ? rs.getDouble("finalGrade") : "Not graded"
                    };
                    tableModel.addRow(row);
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading projects: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void viewDetails() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a project!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int projectID = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String query = "SELECT p.*, u.name as supervisor, pr.objectives, pr.methodology " +
                          "FROM projects p " +
                          "LEFT JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                          "LEFT JOIN users u ON s.userID = u.userID " +
                          "LEFT JOIN proposals pr ON p.projectID = pr.projectID " +
                          "WHERE p.projectID = ?";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "Project ID: %d\n" +
                    "Title: %s\n\n" +
                    "Description:\n%s\n\n" +
                    "Status: %s\n" +
                    "Supervisor: %s\n" +
                    "Final Grade: %s\n\n" +
                    "Objectives:\n%s\n\n" +
                    "Methodology:\n%s",
                    rs.getInt("projectID"),
                    rs.getString("title"),
                    rs.getString("description") != null ? rs.getString("description") : "N/A",
                    rs.getString("status"),
                    rs.getString("supervisor") != null ? rs.getString("supervisor") : "N/A",
                    rs.getObject("finalGrade") != null ? rs.getDouble("finalGrade") : "Not graded yet",
                    rs.getString("objectives") != null ? rs.getString("objectives") : "N/A",
                    rs.getString("methodology") != null ? rs.getString("methodology") : "N/A"
                );
                
                JTextArea textArea = new JTextArea(details);
                textArea.setEditable(false);
                textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Project Details",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading project details: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}