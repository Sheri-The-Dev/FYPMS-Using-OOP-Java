/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class SystemStatsDialog extends JDialog {
    
    public SystemStatsDialog(JFrame parent) {
        super(parent, "System Statistics", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(142, 68, 173));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📊 System Statistics");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        JPanel statsPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        statsPanel.setBackground(Color.WHITE);
        
        statsPanel.add(createStatCard("Total Users", String.valueOf(getCount("users")), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Projects", String.valueOf(getCount("projects")), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Pending Proposals", String.valueOf(getCountWhere("proposals", "status = 'Pending'")), new Color(231, 76, 60)));
        statsPanel.add(createStatCard("Completed Projects", String.valueOf(getCountWhere("projects", "status = 'Completed'")), new Color(241, 196, 15)));
        statsPanel.add(createStatCard("Students", String.valueOf(getCount("students")), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Supervisors", String.valueOf(getCount("supervisors")), new Color(52, 73, 94)));
        statsPanel.add(createStatCard("Committee Members", String.valueOf(getCount("committee_members")), new Color(26, 188, 156)));
        statsPanel.add(createStatCard("Evaluations", String.valueOf(getCount("evaluations")), new Color(230, 126, 34)));
        
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
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
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
    
    private int getCount(String table) {
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }
    
    private int getCountWhere(String table, String condition) {
        try {
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table + " WHERE " + condition);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }
}