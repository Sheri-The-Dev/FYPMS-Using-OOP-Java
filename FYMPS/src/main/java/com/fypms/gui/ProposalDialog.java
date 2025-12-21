package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class ProposalDialog extends JDialog {
    
    private int userID;
    private JTextField titleField;
    private JTextArea objectivesArea, methodologyArea, descriptionArea;
    private JComboBox<String> supervisorCombo;
    
    public ProposalDialog(JFrame parent, int userID) {
        super(parent, "Submit Project Proposal", true);
        this.userID = userID;
        initComponents();
        loadSupervisors();
    }
    
    private void initComponents() {
        setSize(800, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📝 Submit Project Proposal");
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
        
        // Project Title
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Project Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(titleLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        titleField = new JTextField(30);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(titleField, gbc);
        
        // Project Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(descLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);
        
        // Objectives
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel objLabel = new JLabel("Objectives:");
        objLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(objLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        objectivesArea = new JTextArea(5, 30);
        objectivesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        objectivesArea.setLineWrap(true);
        objectivesArea.setWrapStyleWord(true);
        JScrollPane objScroll = new JScrollPane(objectivesArea);
        formPanel.add(objScroll, gbc);
        
        // Methodology
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel methLabel = new JLabel("Methodology:");
        methLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(methLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.BOTH;
        methodologyArea = new JTextArea(5, 30);
        methodologyArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        methodologyArea.setLineWrap(true);
        methodologyArea.setWrapStyleWord(true);
        JScrollPane methScroll = new JScrollPane(methodologyArea);
        formPanel.add(methScroll, gbc);
        
        // Supervisor Selection
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel supLabel = new JLabel("Select Supervisor:");
        supLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(supLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        supervisorCombo = new JComboBox<>();
        supervisorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(supervisorCombo, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton submitBtn = new JButton("✓ Submit Proposal");
        submitBtn.setPreferredSize(new Dimension(170, 45));
        submitBtn.setBackground(new Color(46, 204, 113));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(e -> submitProposal());
        
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
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadSupervisors() {
        supervisorCombo.removeAllItems();
        supervisorCombo.addItem("-- Select Supervisor --");
        
        try {
            String query = "SELECT u.userID, u.name, s.currentLoad, s.maxCapacity " +
                          "FROM users u " +
                          "JOIN supervisors s ON u.userID = s.userID " +
                          "WHERE u.userType = 'Supervisor' AND s.currentLoad < s.maxCapacity";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                int userId = rs.getInt("userID");
                String name = rs.getString("name");
                int load = rs.getInt("currentLoad");
                int capacity = rs.getInt("maxCapacity");
                
                supervisorCombo.addItem(userId + " - " + name + " (" + load + "/" + capacity + ")");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading supervisors: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void submitProposal() {
        // Validation
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter project title!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (descriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter project description!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (objectivesArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter objectives!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (methodologyArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter methodology!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (supervisorCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a supervisor!", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get supervisor ID from combo box selection
            String selected = (String) supervisorCombo.getSelectedItem();
            int supervisorUserId = Integer.parseInt(selected.split(" - ")[0]);
            
            // Get student ID from users table
            String studentQuery = "SELECT studentID FROM students WHERE userID = ?";
            PreparedStatement studentPst = DatabaseConnection.getInstance().prepareStatement(studentQuery);
            studentPst.setInt(1, userID);
            ResultSet studentRs = studentPst.executeQuery();
            
            if (!studentRs.next()) {
                JOptionPane.showMessageDialog(this, 
                    "Student record not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String studentID = studentRs.getString("studentID");
            
            // Get supervisor ID
            String supQuery = "SELECT supervisorID FROM supervisors WHERE userID = ?";
            PreparedStatement supPst = DatabaseConnection.getInstance().prepareStatement(supQuery);
            supPst.setInt(1, supervisorUserId);
            ResultSet supRs = supPst.executeQuery();
            
            if (!supRs.next()) {
                JOptionPane.showMessageDialog(this, 
                    "Supervisor record not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String supervisorID = supRs.getString("supervisorID");
            
            // Start transaction
            Connection conn = (Connection) DatabaseConnection.getInstance();
            conn.setAutoCommit(false);
            
            try {
                // 1. Insert into projects table
                String projectQuery = "INSERT INTO projects (title, description, status, supervisorID) VALUES (?, ?, 'Proposed', ?)";
                PreparedStatement projectPst = conn.prepareStatement(projectQuery, Statement.RETURN_GENERATED_KEYS);
                projectPst.setString(1, titleField.getText().trim());
                projectPst.setString(2, descriptionArea.getText().trim());
                projectPst.setString(3, supervisorID);
                projectPst.executeUpdate();
                
                // Get generated project ID
                ResultSet rs = projectPst.getGeneratedKeys();
                int projectID = 0;
                if (rs.next()) {
                    projectID = rs.getInt(1);
                }
                
                // 2. Insert into proposals table
                String proposalQuery = "INSERT INTO proposals (projectID, submissionDate, status, objectives, methodology, supervisorID, submittedBy) " +
                                      "VALUES (?, CURDATE(), 'Pending', ?, ?, ?, ?)";
                PreparedStatement proposalPst = conn.prepareStatement(proposalQuery);
                proposalPst.setInt(1, projectID);
                proposalPst.setString(2, objectivesArea.getText().trim());
                proposalPst.setString(3, methodologyArea.getText().trim());
                proposalPst.setString(4, supervisorID);
                proposalPst.setString(5, studentID);
                proposalPst.executeUpdate();
                
                // 3. Update supervisor load
                String updateLoadQuery = "UPDATE supervisors SET currentLoad = currentLoad + 1 WHERE supervisorID = ?";
                PreparedStatement loadPst = conn.prepareStatement(updateLoadQuery);
                loadPst.setString(1, supervisorID);
                loadPst.executeUpdate();
                
                // Commit transaction
                conn.commit();
                conn.setAutoCommit(true);
                
                JOptionPane.showMessageDialog(this,
                    "✅ Proposal Submitted Successfully!\n\n" +
                    "Project ID: " + projectID + "\n" +
                    "Title: " + titleField.getText() + "\n" +
                    "Status: Pending Review\n\n" +
                    "Your supervisor will review your proposal soon.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                
            } catch (SQLException e) {
                // Rollback on error
                conn.rollback();
                conn.setAutoCommit(true);
                throw e;
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error submitting proposal: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}