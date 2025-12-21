/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class ProfileDialog extends JDialog {
    private int userID;
    private JTextField nameField, emailField, deptField;
    private JPasswordField oldPasswordField, newPasswordField, confirmPasswordField;
    
    public ProfileDialog(JFrame parent, int userID) {
        super(parent, "My Profile", true);
        this.userID = userID;
        initComponents();
        loadUserData();
    }
    
    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("👤 User Profile");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Profile Information Section
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel profileLabel = new JLabel("Profile Information");
        profileLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(profileLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        deptField = new JTextField(20);
        formPanel.add(deptField, gbc);
        
        // Change Password Section
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JLabel passwordLabel = new JLabel("Change Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Old Password
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Old Password:"), gbc);
        gbc.gridx = 1;
        oldPasswordField = new JPasswordField(20);
        formPanel.add(oldPasswordField, gbc);
        
        // New Password
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setBackground(new Color(46, 204, 113));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateBtn.setBorderPainted(false);
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(e -> updateProfile());
        
        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.setBackground(new Color(52, 152, 219));
        changePasswordBtn.setForeground(Color.WHITE);
        changePasswordBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        changePasswordBtn.setBorderPainted(false);
        changePasswordBtn.setFocusPainted(false);
        changePasswordBtn.addActionListener(e -> changePassword());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(changePasswordBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadUserData() {
        try {
            String query = "SELECT name, email, department FROM users WHERE userID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                emailField.setText(rs.getString("email"));
                deptField.setText(rs.getString("department"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateProfile() {
        try {
            String query = "UPDATE users SET name = ?, email = ?, department = ? WHERE userID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setString(1, nameField.getText());
            pst.setString(2, emailField.getText());
            pst.setString(3, deptField.getText());
            pst.setInt(4, userID);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all password fields!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Verify old password
            String verifyQuery = "SELECT userID FROM users WHERE userID = ? AND password = ?";
            PreparedStatement verifyPst = DatabaseConnection.getInstance().prepareStatement(verifyQuery);
            verifyPst.setInt(1, userID);
            verifyPst.setString(2, oldPassword);
            ResultSet rs = verifyPst.executeQuery();
            
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Old password is incorrect!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update password
            String updateQuery = "UPDATE users SET password = ? WHERE userID = ?";
            PreparedStatement updatePst = DatabaseConnection.getInstance().prepareStatement(updateQuery);
            updatePst.setString(1, newPassword);
            updatePst.setInt(2, userID);
            
            int rows = updatePst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                oldPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error changing password: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
