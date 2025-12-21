package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class AddUserDialog extends JDialog {
    
    private JTextField usernameField, nameField, emailField, deptField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private JTextField studentIDField, supervisorIDField, adminIDField, committeeIDField;
    private JPanel specificPanel;
    
    public AddUserDialog(JFrame parent) {
        super(parent, "Add New User", true);
        initComponents();
    }
    
    private void initComponents() {
        setSize(600, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("➕ Add New User");
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
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Department
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        deptField = new JTextField(20);
        formPanel.add(deptField, gbc);
        
        // User Type
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Student", "Supervisor", "Administrator", "CommitteeMember"});
        userTypeCombo.addActionListener(e -> updateSpecificFields());
        formPanel.add(userTypeCombo, gbc);
        
        // Specific Fields Panel
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        specificPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        specificPanel.setBackground(Color.WHITE);
        specificPanel.setBorder(BorderFactory.createTitledBorder("Type-Specific Fields"));
        formPanel.add(specificPanel, gbc);
        
        updateSpecificFields();
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveBtn = new JButton("💾 Save User");
        saveBtn.setPreferredSize(new Dimension(140, 40));
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveUser());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(100, 40));
        cancelBtn.setBackground(new Color(149, 165, 166));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateSpecificFields() {
        specificPanel.removeAll();
        String userType = (String) userTypeCombo.getSelectedItem();
        
        if ("Student".equals(userType)) {
            specificPanel.add(new JLabel("Student ID:"));
            studentIDField = new JTextField();
            specificPanel.add(studentIDField);
            specificPanel.add(new JLabel("CGPA:"));
            specificPanel.add(new JTextField("0.00"));
        } else if ("Supervisor".equals(userType)) {
            specificPanel.add(new JLabel("Supervisor ID:"));
            supervisorIDField = new JTextField();
            specificPanel.add(supervisorIDField);
            specificPanel.add(new JLabel("Max Capacity:"));
            specificPanel.add(new JTextField("5"));
        } else if ("Administrator".equals(userType)) {
            specificPanel.add(new JLabel("Admin ID:"));
            adminIDField = new JTextField();
            specificPanel.add(adminIDField);
        } else if ("CommitteeMember".equals(userType)) {
            specificPanel.add(new JLabel("Committee ID:"));
            committeeIDField = new JTextField();
            specificPanel.add(committeeIDField);
        }
        
        specificPanel.revalidate();
        specificPanel.repaint();
    }
    
    private void saveUser() {
        // Validation
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Please enter password!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = null;
        
        try {
            // FIXED: No casting needed - getInstance() returns Connection
            conn = DatabaseConnection.getInstance();
            conn.setAutoCommit(false);
            
            // Insert into users table
            String userQuery = "INSERT INTO users (username, password, name, email, userType) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement userPst = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userPst.setString(1, usernameField.getText().trim());
            userPst.setString(2, new String(passwordField.getPassword()));
            userPst.setString(3, nameField.getText().trim());
            userPst.setString(4, emailField.getText().trim());
            userPst.setString(5, (String) userTypeCombo.getSelectedItem());
            userPst.executeUpdate();
            
            // Get generated userID
            ResultSet rs = userPst.getGeneratedKeys();
            int userID = 0;
            if (rs.next()) {
                userID = rs.getInt(1);
            }
            
            // Insert into specific table based on user type
            String userType = (String) userTypeCombo.getSelectedItem();
            
            if ("Student".equals(userType) && studentIDField != null) {
                String query = "INSERT INTO students (studentID, userID, cgpa) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, studentIDField.getText().trim());
                pst.setInt(2, userID);
                pst.setDouble(3, 0.00);
                pst.executeUpdate();
            } else if ("Supervisor".equals(userType) && supervisorIDField != null) {
                String query = "INSERT INTO supervisors (supervisorID, userID, maxCapacity, currentLoad) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, supervisorIDField.getText().trim());
                pst.setInt(2, userID);
                pst.setInt(3, 5);
                pst.setInt(4, 0);
                pst.executeUpdate();
            } else if ("Administrator".equals(userType) && adminIDField != null) {
                String query = "INSERT INTO administrators (adminID, userID) VALUES (?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, adminIDField.getText().trim());
                pst.setInt(2, userID);
                pst.executeUpdate();
            } else if ("CommitteeMember".equals(userType) && committeeIDField != null) {
                String query = "INSERT INTO committee_members (committeeID, userID) VALUES (?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, committeeIDField.getText().trim());
                pst.setInt(2, userID);
                pst.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
            JOptionPane.showMessageDialog(this,
                "✅ User created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            JOptionPane.showMessageDialog(this,
                "❌ Error creating user: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}