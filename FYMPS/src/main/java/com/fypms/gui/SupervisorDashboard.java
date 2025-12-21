package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.fypms.services.*;
import java.sql.*;

public class SupervisorDashboard extends JFrame {
    
    private int userID;
    private String username;
    private ProjectService projectService;
    private ProposalService proposalService;
    
    public SupervisorDashboard(int userID, String username) {
        this.userID = userID;
        this.username = username;
        this.projectService = new ProjectService();
        this.proposalService = new ProposalService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("FYPMS - Supervisor Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createMenuBar();
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(230, 126, 34));
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        
        JMenu projectsMenu = new JMenu("Projects");
        projectsMenu.setForeground(Color.WHITE);
        JMenuItem viewProjectsItem = new JMenuItem("Supervised Projects");
        viewProjectsItem.addActionListener(e -> viewSupervisedProjects());
        projectsMenu.add(viewProjectsItem);
        
        JMenu proposalsMenu = new JMenu("Proposals");
        proposalsMenu.setForeground(Color.WHITE);
        JMenuItem pendingItem = new JMenuItem("Pending Proposals");
        pendingItem.addActionListener(e -> viewProposals());
        proposalsMenu.add(pendingItem);
        
        JMenu evaluationMenu = new JMenu("Evaluation");
        evaluationMenu.setForeground(Color.WHITE);
        JMenuItem evaluateItem = new JMenuItem("Evaluate Projects");
        evaluateItem.addActionListener(e -> evaluateProjects());
        evaluationMenu.add(evaluateItem);
        
        menuBar.add(fileMenu);
        menuBar.add(projectsMenu);
        menuBar.add(proposalsMenu);
        menuBar.add(evaluationMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 126, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Supervisor Portal");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setForeground(new Color(236, 240, 241));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(welcomeLabel);
        textPanel.add(roleLabel);
        
        leftPanel.add(textPanel);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        
        JButton statsBtn = new JButton("📊 Statistics");
        statsBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statsBtn.setForeground(Color.WHITE);
        statsBtn.setBackground(new Color(211, 84, 0));
        statsBtn.setBorderPainted(false);
        statsBtn.setFocusPainted(false);
        statsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statsBtn.addActionListener(e -> showStatistics());
        
        rightPanel.add(dateLabel);
        rightPanel.add(statsBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(new Color(236, 240, 241));
        
        contentPanel.add(createProfessionalCard(
            "📚 Supervised Projects",
            "Manage your supervised projects",
            new Color(41, 128, 185),
            getSupervisedProjectCount() + " active projects",
            e -> viewSupervisedProjects()
        ));
        
        contentPanel.add(createProfessionalCard(
            "📋 Pending Proposals",
            "Review student proposals",
            new Color(231, 76, 60),
            getPendingProposalCount() + " proposals pending",
            e -> viewProposals()
        ));
        
        contentPanel.add(createProfessionalCard(
            "⭐ Evaluate Projects",
            "Submit project evaluations",
            new Color(46, 204, 113),
            "Grade student work",
            e -> evaluateProjects()
        ));
        
        contentPanel.add(createProfessionalCard(
            "👤 My Profile",
            "View and update your profile",
            new Color(149, 165, 166),
            "Manage account settings",
            e -> viewProfile()
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
        
        card.addMouseListener(new MouseAdapter() {
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
        
        JLabel capacityLabel = new JLabel("Capacity: " + getCurrentLoad() + "/5");
        capacityLabel.setForeground(Color.WHITE);
        capacityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel copyrightLabel = new JLabel("© 2024 FYPMS");
        copyrightLabel.setForeground(new Color(189, 195, 199));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        footerPanel.add(statusLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(capacityLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(copyrightLabel);
        
        return footerPanel;
    }
    
    private int getSupervisedProjectCount() {
        try {
            ResultSet rs = projectService.getProjectsBySupervisor(userID);
            int count = 0;
            while (rs != null && rs.next()) count++;
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    private int getPendingProposalCount() {
        try {
            ResultSet rs = proposalService.getPendingProposals(userID);
            int count = 0;
            while (rs != null && rs.next()) count++;
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    private int getCurrentLoad() {
        return getSupervisedProjectCount();
    }
    
    private void viewSupervisedProjects() {
        ProjectListDialog dialog = new ProjectListDialog(this, userID, "Supervisor");
        dialog.setVisible(true);
    }
    
    private void viewProposals() {
        ProposalApprovalDialog dialog = new ProposalApprovalDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void evaluateProjects() {
        EvaluationDialog dialog = new EvaluationDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void viewProfile() {
        ProfileDialog dialog = new ProfileDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void showStatistics() {
        SupervisorStatsDialog dialog = new SupervisorStatsDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}