/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class CommitteeFeedbackDialog extends JDialog {
    
    private int userID;
    private JComboBox<String> projectCombo;
    private JTextArea feedbackArea;
    
    public CommitteeFeedbackDialog(JFrame parent, int userID) {
        super(parent, "Provide Feedback", true);
        this.userID = userID;
        initComponents();
        loadProjects();
    }
    
    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("💬 Provide Project Feedback");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Project Selection
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel projectLabel = new JLabel("Select Project:");
        projectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(projectLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        projectCombo = new JComboBox<>();
        projectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(projectCombo, gbc);
        
        // Feedback Text Area
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(feedbackLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        feedbackArea = new JTextArea(12, 40);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        formPanel.add(scrollPane, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton submitBtn = new JButton("✓ Submit Feedback");
        submitBtn.setPreferredSize(new Dimension(170, 45));
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(e -> submitFeedback());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(120, 45));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProjects() {
        projectCombo.removeAllItems();
        projectCombo.addItem("-- Select Project --");
        
        try {
            String query = "SELECT projectID, title FROM projects WHERE status IN ('Approved', 'InProgress', 'Completed')";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                projectCombo.addItem(rs.getInt("projectID") + " - " + rs.getString("title"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading projects: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void submitFeedback() {
        if (projectCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a project!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (feedbackArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter feedback text!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get project ID from combo box
            String selected = (String) projectCombo.getSelectedItem();
            int projectID = Integer.parseInt(selected.split(" - ")[0]);
            
            // Get committee ID
            String commitQuery = "SELECT committeeID FROM committee_members WHERE userID = ?";
            PreparedStatement commitPst = DatabaseConnection.getInstance().prepareStatement(commitQuery);
            commitPst.setInt(1, userID);
            ResultSet commitRs = commitPst.executeQuery();
            
            if (commitRs.next()) {
                String committeeID = commitRs.getString("committeeID");
                
                // Insert feedback
                String query = "INSERT INTO feedback (projectID, text, givenBy, feedbackDate) VALUES (?, ?, ?, CURDATE())";
                PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, projectID);
                pst.setString(2, feedbackArea.getText().trim());
                pst.setString(3, committeeID);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this,
                    "Feedback submitted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                feedbackArea.setText("");
                projectCombo.setSelectedIndex(0);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error submitting feedback: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}