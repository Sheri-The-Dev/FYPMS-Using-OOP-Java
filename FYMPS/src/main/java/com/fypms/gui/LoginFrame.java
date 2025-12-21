package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.fypms.services.AuthenticationService;
import java.sql.*;

public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;
    private AuthenticationService authService;
    
    public LoginFrame() {
        this.authService = new AuthenticationService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("FYPMS - Login");
        setSize(500, 650); // Increased height for user type selector
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(500, 150));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("FYPMS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Final Year Project Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        headerPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        headerPanel.add(subtitleLabel, gbc);
        
        // Login Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Welcome Label
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        JLabel instructionLabel = new JLabel("Please login to your account");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(127, 140, 141));
        formPanel.add(instructionLabel, gbc);
        
        // User Type Selection (NEW)
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(25, 10, 5, 10);
        JLabel userTypeLabel = new JLabel("Login As:");
        userTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTypeLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(userTypeLabel, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        userTypeCombo = new JComboBox<>(new String[]{
            "Student",
            "Supervisor", 
            "Administrator",
            "Committee Member"
        });
        userTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTypeCombo.setPreferredSize(new Dimension(320, 45));
        userTypeCombo.setBackground(Color.WHITE);
        userTypeCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Add icons for each user type
        userTypeCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                String userType = (String) value;
                String icon = "";
                switch (userType) {
                    case "Student": icon = "🎓 "; break;
                    case "Supervisor": icon = "👨‍🏫 "; break;
                    case "Administrator": icon = "👔 "; break;
                    case "Committee Member": icon = "👥 "; break;
                }
                label.setText(icon + userType);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        
        formPanel.add(userTypeCombo, gbc);
        
        // Username
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(usernameLabel, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 10, 10);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(320, 45));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 5, 10);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 10, 10, 10);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(320, 45));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        passwordField.addActionListener(e -> performLogin());
        formPanel.add(passwordField, gbc);
        
        // Login Button
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setPreferredSize(new Dimension(320, 45));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
        });
        
        loginButton.addActionListener(e -> performLogin());
        formPanel.add(loginButton, gbc);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel infoLabel = new JLabel("© 2024 FYPMS - All Rights Reserved");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoPanel.add(infoLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedUserType = (String) userTypeCombo.getSelectedItem();
        
        // Convert "Committee Member" to "CommitteeMember" for database
        if ("Committee Member".equals(selectedUserType)) {
            selectedUserType = "CommitteeMember";
        }
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter username and password!",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Pass the selected user type to authenticate
            ResultSet rs = authService.authenticate(username, password, selectedUserType);
            
            if (rs != null && rs.next()) {
                int userID = rs.getInt("userID");
                String name = rs.getString("name");
                String userType = rs.getString("userType");
                
                // Verify user type matches selection
                if (!userType.equals(selectedUserType)) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid user type! You selected '" + userTypeCombo.getSelectedItem() + 
                        "' but this account is a '" + userType + "'.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    return;
                }
                
                // Close login window
                this.dispose();
                
                // Open appropriate dashboard based on user type
                switch (userType) {
                    case "Student":
                        new StudentDashboard(userID, name).setVisible(true);
                        break;
                        
                    case "Supervisor":
                        new SupervisorDashboard(userID, name).setVisible(true);
                        break;
                        
                    case "Administrator":
                        new AdminDashboard(userID, name).setVisible(true);
                        break;
                        
                    case "CommitteeMember":
                        new CommitteeDashboard(userID, name).setVisible(true);
                        break;
                        
                    default:
                        JOptionPane.showMessageDialog(this,
                            "Unknown user type: " + userType,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        new LoginFrame().setVisible(true);
                }
                
                System.out.println("✅ Login successful: " + name + " (" + userType + ")");
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username, password, or user type!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}