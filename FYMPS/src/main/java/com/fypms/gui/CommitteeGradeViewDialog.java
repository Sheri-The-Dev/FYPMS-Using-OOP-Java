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

public class CommitteeGradeViewDialog extends JDialog {
    
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    
    public CommitteeGradeViewDialog(JFrame parent) {
        super(parent, "View All Grades", true);
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
        JLabel headerLabel = new JLabel("📊 Project Grades");
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
        gradeTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gradeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        gradeTable.getTableHeader().setBackground(new Color(52, 73, 94));
        gradeTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(52, 152, 219));
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
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadGrades() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT p.title, e.technicalScore, e.documentationScore, " +
                          "e.presentationScore, e.totalScore, p.status " +
                          "FROM evaluations e " +
                          "JOIN projects p ON e.projectID = p.projectID " +
                          "ORDER BY e.evaluationDate DESC";
            
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("title"),
                    rs.getDouble("technicalScore"),
                    rs.getDouble("documentationScore"),
                    rs.getDouble("presentationScore"),
                    String.format("%.2f", rs.getDouble("totalScore")),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading grades: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}