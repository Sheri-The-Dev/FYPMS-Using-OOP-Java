package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.fypms.services.*;
import java.sql.*;

public class StudentDashboard extends JFrame {
    
    private int userID;
    private String username;
    private ProjectService projectService;
    private DocumentService documentService;
    private GradeService gradeService;
    
    public StudentDashboard(int userID, String username) {
        this.userID = userID;
        this.username = username;
        this.projectService = new ProjectService();
        this.documentService = new DocumentService();
        this.gradeService = new GradeService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("FYPMS - Student Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create menu bar
        createMenuBar();
        
        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header Panel - Professional Design
        JPanel headerPanel = createHeaderPanel();
        
        // Content Panel - Cards Grid
        JPanel contentPanel = createContentPanel();
        
        // Footer Panel - Statistics
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 73, 94));
        menuBar.setBorderPainted(false);
        
        // File Menu
        JMenu fileMenu = createStyledMenu("File");
        JMenuItem profileItem = createStyledMenuItem("My Profile", "user.png");
        profileItem.addActionListener(e -> showProfile());
        JMenuItem settingsItem = createStyledMenuItem("Settings", "settings.png");
        JMenuItem logoutItem = createStyledMenuItem("Logout", "logout.png");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = createStyledMenuItem("Exit", "exit.png");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(profileItem);
        fileMenu.add(settingsItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        
        // Project Menu
        JMenu projectMenu = createStyledMenu("Projects");
        JMenuItem viewProjectsItem = createStyledMenuItem("My Projects", "folder.png");
        viewProjectsItem.addActionListener(e -> viewMyProjects());
        JMenuItem createProjectItem = createStyledMenuItem("Create New Project", "add.png");
        
        projectMenu.add(viewProjectsItem);
        projectMenu.add(createProjectItem);
        
        // Proposal Menu
        JMenu proposalMenu = createStyledMenu("Proposals");
        JMenuItem submitProposalItem = createStyledMenuItem("Submit Proposal", "upload.png");
        submitProposalItem.addActionListener(e -> submitProposal());
        JMenuItem viewProposalsItem = createStyledMenuItem("View My Proposals", "list.png");
        
        proposalMenu.add(submitProposalItem);
        proposalMenu.add(viewProposalsItem);
        
        // Documents Menu
        JMenu documentsMenu = createStyledMenu("Documents");
        JMenuItem uploadDocItem = createStyledMenuItem("Upload Document", "upload.png");
        uploadDocItem.addActionListener(e -> uploadDocument());
        JMenuItem viewDocsItem = createStyledMenuItem("View Documents", "docs.png");
        viewDocsItem.addActionListener(e -> viewDocuments());
        
        documentsMenu.add(uploadDocItem);
        documentsMenu.add(viewDocsItem);
        
        // Grades Menu
        JMenu gradesMenu = createStyledMenu("Grades");
        JMenuItem viewGradesItem = createStyledMenuItem("View My Grades", "grade.png");
        viewGradesItem.addActionListener(e -> viewGrades());
        JMenuItem evaluationsItem = createStyledMenuItem("View Evaluations", "star.png");
        
        gradesMenu.add(viewGradesItem);
        gradesMenu.add(evaluationsItem);
        
        // Help Menu
        JMenu helpMenu = createStyledMenu("Help");
        JMenuItem guideItem = createStyledMenuItem("User Guide", "help.png");
        JMenuItem aboutItem = createStyledMenuItem("About", "info.png");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(guideItem);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(projectMenu);
        menuBar.add(proposalMenu);
        menuBar.add(documentsMenu);
        menuBar.add(gradesMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return menu;
    }
    
    private JMenuItem createStyledMenuItem(String text, String icon) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return item;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Left side - Welcome message
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Student Portal");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setForeground(new Color(236, 240, 241));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(welcomeLabel);
        textPanel.add(roleLabel);
        
        leftPanel.add(textPanel);
        
        // Right side - Date and notifications
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        
        JButton notificationBtn = new JButton("🔔 Notifications");
        notificationBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        notificationBtn.setForeground(Color.WHITE);
        notificationBtn.setBackground(new Color(52, 152, 219));
        notificationBtn.setBorderPainted(false);
        notificationBtn.setFocusPainted(false);
        notificationBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notificationBtn.addActionListener(e -> showNotifications());
        
        rightPanel.add(dateLabel);
        rightPanel.add(notificationBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(new Color(236, 240, 241));
        
        // Create Dashboard Cards with Icons and Hover Effects
        contentPanel.add(createProfessionalCard(
            "📁 My Projects", 
            "View and manage your projects",
            new Color(46, 204, 113),
            "View " + getProjectCount() + " projects",
            e -> viewMyProjects()
        ));
        
        contentPanel.add(createProfessionalCard(
            "📝 Submit Proposal", 
            "Submit your project proposal",
            new Color(52, 152, 219),
            "Create new proposal",
            e -> submitProposal()
        ));
        
        contentPanel.add(createProfessionalCard(
            "📄 Documents", 
            "Upload and manage documents",
            new Color(155, 89, 182),
            getDocumentCount() + " documents uploaded",
            e -> viewDocuments()
        ));
        
        contentPanel.add(createProfessionalCard(
            "🎓 My Grades", 
            "View your project grades",
            new Color(241, 196, 15),
            "Check evaluation results",
            e -> viewGrades()
        ));
        
        return contentPanel;
    }
    
    private JPanel createProfessionalCard(String title, String description, 
                                         Color bgColor, String info, ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), bgColor.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        
        // Title and description
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(236, 240, 241));
        
        JLabel infoLabel = new JLabel(info);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        infoLabel.setForeground(new Color(255, 255, 255, 200));
        
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        textPanel.add(infoLabel);
        
        // Action button
        JButton actionBtn = new JButton("Open →");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        actionBtn.setForeground(bgColor);
        actionBtn.setBackground(Color.WHITE);
        actionBtn.setBorderPainted(false);
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(action);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(actionBtn);
        
        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            private Color originalColor = bgColor;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(23, 23, 23, 23)
                ));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
                card.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, null));
            }
        });
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        footerPanel.setBackground(new Color(52, 73, 94));
        
        JLabel statusLabel = new JLabel("Status: Active");
        statusLabel.setForeground(new Color(46, 204, 113));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JLabel versionLabel = new JLabel("FYPMS v1.0");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel copyrightLabel = new JLabel("© 2024 Final Year Project Management System");
        copyrightLabel.setForeground(new Color(189, 195, 199));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        footerPanel.add(statusLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(versionLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(copyrightLabel);
        
        return footerPanel;
    }
    
    // Helper methods to get counts from database
    private int getProjectCount() {
        try {
            ResultSet rs = projectService.getProjectsByStudent(userID);
            int count = 0;
            while (rs != null && rs.next()) count++;
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    private int getDocumentCount() {
        try {
            ResultSet rs = documentService.getDocumentsByStudent(userID);
            int count = 0;
            while (rs != null && rs.next()) count++;
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    // Action Methods
    private void viewMyProjects() {
        ProjectListDialog dialog = new ProjectListDialog(this, userID, "Student");
        dialog.setVisible(true);
    }
    
    private void submitProposal() {
        ProposalDialog dialog = new ProposalDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void uploadDocument() {
        DocumentUploadDialog dialog = new DocumentUploadDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void viewDocuments() {
        DocumentListDialog dialog = new DocumentListDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void viewGrades() {
        GradeViewDialog dialog = new GradeViewDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void showProfile() {
        ProfileDialog dialog = new ProfileDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void showNotifications() {
        NotificationDialog dialog = new NotificationDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Final Year Project Management System\n" +
            "Version 1.0\n\n" +
            "A comprehensive system for managing\n" +
            "final year projects, proposals, and evaluations.\n\n" +
            "© 2024 All Rights Reserved",
            "About FYPMS",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}