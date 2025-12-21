/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import com.fypms.services.NotificationService;
import java.sql.*;

public class NotificationDialog extends JDialog {
    private JTable notificationTable;
    private DefaultTableModel tableModel;
    private NotificationService notificationService;
    private int userID;
    
    public NotificationDialog(JFrame parent, int userID) {
        super(parent, "Notifications", true);
        this.userID = userID;
        this.notificationService = new NotificationService();
        initComponents();
        loadNotifications();
    }
    
    private void initComponents() {
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("🔔 Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"Type", "Message", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        notificationTable = new JTable(tableModel);
        notificationTable.setRowHeight(40);
        notificationTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        notificationTable.getTableHeader().setBackground(new Color(52, 73, 94));
        notificationTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton markReadBtn = new JButton("Mark as Read");
        markReadBtn.setBackground(new Color(46, 204, 113));
        markReadBtn.setForeground(Color.WHITE);
        markReadBtn.setFocusPainted(false);
        markReadBtn.addActionListener(e -> markAsRead());
        
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(markReadBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadNotifications() {
        tableModel.setRowCount(0);
        
        // Add sample notifications (you can replace with database query)
        addSampleNotifications();
    }
    
    private void addSampleNotifications() {
        tableModel.addRow(new Object[]{"Info", "New proposal submitted for review", "2024-12-09", "Unread"});
        tableModel.addRow(new Object[]{"Success", "Your project has been approved", "2024-12-08", "Unread"});
        tableModel.addRow(new Object[]{"Warning", "Deadline approaching for document submission", "2024-12-07", "Read"});
        tableModel.addRow(new Object[]{"Info", "Meeting scheduled for tomorrow at 10 AM", "2024-12-06", "Read"});
    }
    
    private void markAsRead() {
        int selectedRow = notificationTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.setValueAt("Read", selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Notification marked as read", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a notification", 
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
