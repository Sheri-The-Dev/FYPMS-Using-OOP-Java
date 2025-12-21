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

public class GradeViewDialog extends JDialog {
    
    private int userID;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    
    public GradeViewDialog(JFrame parent, int userID) {
        super(parent, "My Grades", true);
        this.userID = userID;
        initComponents();
        loadGrades();
    }
    
    private void initComponents() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("🎓 My Project Grades");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"Project", "Technical", "Documentation", "Presentation", "Total Score", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        gradeTable = new JTable(tableModel);
        gradeTable.setRowHeight(35);
        gradeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gradeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        gradeTable.getTableHeader().setBackground(new Color(52, 73, 94));
        gradeTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        statsPanel.setBackground(new Color(236, 240, 241));
        
        statsPanel.add(createStatCard("Projects", "0", new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Average", "0.0", new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Completed", "0", new Color(241, 196, 15)));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewDetailsBtn = new JButton("👁️ View Details");
        viewDetailsBtn.setPreferredSize(new Dimension(150, 40));
        viewDetailsBtn.setBackground(new Color(52, 152, 219));
        viewDetailsBtn.setForeground(Color.WHITE);
        viewDetailsBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        viewDetailsBtn.setBorderPainted(false);
        viewDetailsBtn.setFocusPainted(false);
        viewDetailsBtn.addActionListener(e -> viewDetails());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(46, 204, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadGrades());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelLabel.setForeground(new Color(236, 240, 241));
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void loadGrades() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT p.title, e.technicalScore, e.documentationScore, " +
                          "e.presentationScore, e.totalScore, p.status " +
                          "FROM evaluations e " +
                          "JOIN projects p ON e.projectID = p.projectID " +
                          "JOIN groups g ON p.groupID = g.groupID " +
                          "JOIN group_members gm ON g.groupID = gm.groupID " +
                          "JOIN students s ON gm.studentID = s.studentID " +
                          "WHERE s.userID = ? " +
                          "ORDER BY e.evaluationDate DESC";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            
            int count = 0;
            double totalScore = 0;
            
            while (rs.next()) {
                double score = rs.getDouble("totalScore");
                Object[] row = {
                    rs.getString("title"),
                    rs.getDouble("technicalScore"),
                    rs.getDouble("documentationScore"),
                    rs.getDouble("presentationScore"),
                    String.format("%.2f", score),
                    rs.getString("status")
                };
                tableModel.addRow(row);
                count++;
                totalScore += score;
            }
            
            // Update statistics (implement later if needed)
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading grades: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewDetails() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a grade entry!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String project = (String) tableModel.getValueAt(selectedRow, 0);
        double tech = (double) tableModel.getValueAt(selectedRow, 1);
        double doc = (double) tableModel.getValueAt(selectedRow, 2);
        double pres = (double) tableModel.getValueAt(selectedRow, 3);
        String total = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);
        
        String details = String.format(
            "Project: %s\n\n" +
            "Evaluation Breakdown:\n" +
            "─────────────────────\n" +
            "Technical Score: %.2f / 100\n" +
            "Documentation Score: %.2f / 100\n" +
            "Presentation Score: %.2f / 100\n\n" +
            "Total Score: %s / 100\n" +
            "Status: %s",
            project, tech, doc, pres, total, status
        );
        
        JOptionPane.showMessageDialog(this,
            details,
            "Grade Details",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
