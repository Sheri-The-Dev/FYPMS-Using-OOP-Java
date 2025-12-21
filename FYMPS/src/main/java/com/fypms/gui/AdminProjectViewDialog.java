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

public class AdminProjectViewDialog extends JDialog {
    
    private JTable projectTable;
    private DefaultTableModel tableModel;
    
    public AdminProjectViewDialog(JFrame parent) {
        super(parent, "All Projects", true);
        initComponents();
        loadProjects();
    }
    
    private void initComponents() {
        setSize(1100, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📁 All Projects Overview");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        String[] columns = {"ID", "Title", "Status", "Supervisor", "Grade", "Created"};
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewBtn = new JButton("👁️ View Details");
        styleButton(viewBtn, new Color(52, 152, 219));
        viewBtn.addActionListener(e -> viewDetails());
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        styleButton(deleteBtn, new Color(231, 76, 60));
        deleteBtn.addActionListener(e -> deleteProject());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        styleButton(refreshBtn, new Color(241, 196, 15));
        refreshBtn.addActionListener(e -> loadProjects());
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(149, 165, 166));
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void styleButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }
    
    private void loadProjects() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT p.projectID, p.title, p.status, u.name as supervisor, " +
                          "p.finalGrade, p.createdAt " +
                          "FROM projects p " +
                          "LEFT JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                          "LEFT JOIN users u ON s.userID = u.userID " +
                          "ORDER BY p.projectID DESC";
            
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("projectID"),
                    rs.getString("title"),
                    rs.getString("status"),
                    rs.getString("supervisor") != null ? rs.getString("supervisor") : "N/A",
                    rs.getObject("finalGrade") != null ? rs.getDouble("finalGrade") : "N/A",
                    rs.getTimestamp("createdAt")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewDetails() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a project!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int projectID = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String query = "SELECT p.*, u.name as supervisor FROM projects p " +
                          "LEFT JOIN supervisors s ON p.supervisorID = s.supervisorID " +
                          "LEFT JOIN users u ON s.userID = u.userID WHERE p.projectID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "Project ID: %d\nTitle: %s\n\nDescription:\n%s\n\nStatus: %s\nSupervisor: %s\nGrade: %s",
                    rs.getInt("projectID"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getString("supervisor") != null ? rs.getString("supervisor") : "N/A",
                    rs.getObject("finalGrade") != null ? rs.getDouble("finalGrade") : "Not graded"
                );
                
                JOptionPane.showMessageDialog(this, details, "Project Details", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProject() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a project!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int projectID = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete project: " + title + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "DELETE FROM projects WHERE projectID = ?";
                PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, projectID);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Project deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProjects();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}