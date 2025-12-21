/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class DocumentUploadDialog extends JDialog {
    
    private int userID;
    private JComboBox<String> projectCombo;
    private JTextField titleField;
    private JTextField filePathField;
    private JButton browseButton;
    private File selectedFile;
    
    public DocumentUploadDialog(JFrame parent, int userID) {
        super(parent, "Upload Document", true);
        this.userID = userID;
        initComponents();
        loadProjects();
    }
    
    private void initComponents() {
        setSize(700, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📄 Upload Project Document");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
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
        
        // Document Title
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel titleLabel = new JLabel("Document Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(titleField, gbc);
        
        // File Selection
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel fileLabel = new JLabel("Select File:");
        fileLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(fileLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel filePanel = new JPanel(new BorderLayout(5, 0));
        filePanel.setOpaque(false);
        
        filePathField = new JTextField();
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filePathField.setEditable(false);
        
        browseButton = new JButton("Browse...");
        browseButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseButton.addActionListener(e -> browseFile());
        
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(filePanel, gbc);
        
        // Info Label
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JLabel infoLabel = new JLabel("Supported formats: PDF, DOC, DOCX, TXT, ZIP");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        formPanel.add(infoLabel, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton uploadBtn = new JButton("⬆️ Upload");
        uploadBtn.setPreferredSize(new Dimension(140, 45));
        uploadBtn.setBackground(new Color(46, 204, 113));
        uploadBtn.setForeground(Color.WHITE);
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadBtn.setBorderPainted(false);
        uploadBtn.setFocusPainted(false);
        uploadBtn.addActionListener(e -> uploadDocument());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(120, 45));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(uploadBtn);
        buttonPanel.add(cancelBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProjects() {
        projectCombo.removeAllItems();
        projectCombo.addItem("-- Select Project --");
        
        try {
            String query = "SELECT DISTINCT p.projectID, p.title " +
                          "FROM projects p " +
                          "LEFT JOIN groups g ON p.groupID = g.groupID " +
                          "LEFT JOIN group_members gm ON g.groupID = gm.groupID " +
                          "LEFT JOIN students s ON gm.studentID = s.studentID " +
                          "WHERE s.userID = ? AND p.status IN ('Approved', 'InProgress')";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            
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
    
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Document");
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            
            // Auto-fill title if empty
            if (titleField.getText().isEmpty()) {
                titleField.setText(selectedFile.getName());
            }
        }
    }
    
    private void uploadDocument() {
        // Validation
        if (projectCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a project!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter document title!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get project ID
            String selected = (String) projectCombo.getSelectedItem();
            int projectID = Integer.parseInt(selected.split(" - ")[0]);
            
            // Get file extension
            String fileName = selectedFile.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            
            // In real application, you would upload file to server
            // For now, we just store the file path in database
            String filePath = selectedFile.getAbsolutePath();
            
            // Insert into database
            String query = "INSERT INTO documents (projectID, title, filePath, fileType, uploadDate, uploadedBy) " +
                          "VALUES (?, ?, ?, ?, CURDATE(), ?)";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, projectID);
            pst.setString(2, titleField.getText().trim());
            pst.setString(3, filePath);
            pst.setString(4, fileType);
            pst.setInt(5, userID);
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this,
                "✅ Document uploaded successfully!\n\n" +
                "Title: " + titleField.getText() + "\n" +
                "File: " + fileName,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error uploading document: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
