/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import com.fypms.services.ProjectService;
import java.sql.*;

public class SupervisorStatsDialog extends JDialog {
    private int userID;
    private ProjectService projectService;
    
    public SupervisorStatsDialog(JFrame parent, int userID) {
        super(parent, "Supervisor Statistics", true);
        this.userID = userID;
        this.projectService = new ProjectService();
        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 126, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📊 My Statistics");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        statsPanel.setBackground(Color.WHITE);
        
        int totalProjects = getSupervisedProjectCount();
        int pendingProposals = 3; // Sample data
        int completedProjects = 5; // Sample data
        
        statsPanel.add(createStatCard("Total Projects", String.valueOf(totalProjects), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Pending Proposals", String.valueOf(pendingProposals), new Color(231, 76, 60)));
        statsPanel.add(createStatCard("Completed", String.valueOf(completedProjects), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Current Load", totalProjects + "/5", new Color(241, 196, 15)));
        statsPanel.add(createStatCard("Average Grade", "82.5%", new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Students Supervised", "12", new Color(52, 73, 94)));
        
        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
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
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(new Color(236, 240, 241));
        labelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private int getSupervisedProjectCount() {
        try {
            ResultSet rs = projectService.getProjectsBySupervisor(userID);
            int count = 0;
            while (rs != null && rs.next()) count++;
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
}
