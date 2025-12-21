/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;

public class ReportsDialog extends JDialog {
    
    public ReportsDialog(JFrame parent) {
        super(parent, "System Reports", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📊 Generate Reports");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        JPanel contentPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(Color.WHITE);
        
        JButton btn1 = createReportButton("📈 Project Status Report", new Color(52, 152, 219));
        JButton btn2 = createReportButton("👥 User Activity Report", new Color(46, 204, 113));
        JButton btn3 = createReportButton("📊 Evaluation Report", new Color(241, 196, 15));
        JButton btn4 = createReportButton("📝 Proposal Statistics", new Color(155, 89, 182));
        JButton btn5 = createReportButton("🎓 Grade Distribution", new Color(230, 126, 34));
        
        contentPanel.add(btn1);
        contentPanel.add(btn2);
        contentPanel.add(btn3);
        contentPanel.add(btn4);
        contentPanel.add(btn5);
        
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
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JButton createReportButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(400, 50));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Report generation feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE));
        return btn;
    }
}
