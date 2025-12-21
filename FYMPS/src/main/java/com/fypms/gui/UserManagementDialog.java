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

public class UserManagementDialog extends JDialog {
    
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    public UserManagementDialog(JFrame parent) {
        super(parent, "User Management", true);
        initComponents();
        loadUsers();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("👥 User Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"ID", "Username", "Name", "Department", "User Type", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(52, 73, 94));
        userTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton addBtn = new JButton("➕ Add User");
        addBtn.setPreferredSize(new Dimension(130, 40));
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setBorderPainted(false);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> addUser());
        
        JButton editBtn = new JButton("✏️ Edit");
        editBtn.setPreferredSize(new Dimension(100, 40));
        editBtn.setBackground(new Color(52, 152, 219));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editBtn.setBorderPainted(false);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> editUser());
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        deleteBtn.setPreferredSize(new Dimension(100, 40));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteBtn.setBorderPainted(false);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteUser());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(241, 196, 15));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadUsers());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadUsers() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT userID, username, name, department, userType, createdAt FROM users ORDER BY userID DESC";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("userType"),
                    rs.getTimestamp("createdAt")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading users: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addUser() {
        AddUserDialog dialog = new AddUserDialog((JFrame) getParent());
        dialog.setVisible(true);
        loadUsers(); // Refresh after adding
    }
    
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userID = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        String department = (String) tableModel.getValueAt(selectedRow, 3);
        
        JTextField nameField = new JTextField(name);
        JTextField deptField = new JTextField(department);
        
        Object[] message = {
            "Name:", nameField,
            "Department:", deptField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String query = "UPDATE users SET name = ?, department = ? WHERE userID = ?";
                PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setString(1, nameField.getText());
                pst.setString(2, deptField.getText());
                pst.setInt(3, userID);
                
                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                        "User updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error updating user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userID = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user: " + username + "?",
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "DELETE FROM users WHERE userID = ?";
                PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, userID);
                
                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                        "User deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}