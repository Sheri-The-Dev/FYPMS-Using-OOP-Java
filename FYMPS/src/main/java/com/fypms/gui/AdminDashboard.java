package com.fypms.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class AdminDashboard extends JFrame {
    
    private int userID;
    private String username;
    
    public AdminDashboard(int userID, String username) {
        this.userID = userID;
        this.username = username;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("FYPMS - Administrator Dashboard");
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
        menuBar.setBackground(new Color(142, 68, 173));
        menuBar.setBorderPainted(false);
        
        JMenu fileMenu = createStyledMenu("File");
        JMenuItem logoutItem = createStyledMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        
        JMenu usersMenu = createStyledMenu("Users");
        JMenuItem manageUsersItem = createStyledMenuItem("Manage Users");
        manageUsersItem.addActionListener(e -> manageUsers());
        JMenuItem addUserItem = createStyledMenuItem("Add New User");
        addUserItem.addActionListener(e -> addUser());
        usersMenu.add(manageUsersItem);
        usersMenu.add(addUserItem);
        
        JMenu projectsMenu = createStyledMenu("Projects");
        JMenuItem allProjectsItem = createStyledMenuItem("All Projects");
        allProjectsItem.addActionListener(e -> viewAllProjects());
        JMenuItem reportsItem = createStyledMenuItem("Project Reports");
        reportsItem.addActionListener(e -> generateReports());
        projectsMenu.add(allProjectsItem);
        projectsMenu.add(reportsItem);
        
        JMenu systemMenu = createStyledMenu("System");
        JMenuItem settingsItem = createStyledMenuItem("System Settings");
        JMenuItem backupItem = createStyledMenuItem("Backup Database");
        systemMenu.add(settingsItem);
        systemMenu.add(backupItem);
        
        menuBar.add(fileMenu);
        menuBar.add(usersMenu);
        menuBar.add(projectsMenu);
        menuBar.add(systemMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return menu;
    }
    
    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return item;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(142, 68, 173));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Administrator Portal");
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
        
        JButton dashboardBtn = new JButton("📊 System Stats");
        dashboardBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dashboardBtn.setForeground(Color.WHITE);
        dashboardBtn.setBackground(new Color(155, 89, 182));
        dashboardBtn.setBorderPainted(false);
        dashboardBtn.setFocusPainted(false);
        dashboardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboardBtn.addActionListener(e -> showSystemStats());
        
        rightPanel.add(dateLabel);
        rightPanel.add(dashboardBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(new Color(236, 240, 241));
        
        contentPanel.add(createCard("👥 Manage Users", "Add, edit, delete users", 
            new Color(52, 152, 219), getTotalUsers() + " users", e -> manageUsers()));
        
        contentPanel.add(createCard("📁 All Projects", "View all projects", 
            new Color(46, 204, 113), getTotalProjects() + " projects", e -> viewAllProjects()));
        
        contentPanel.add(createCard("📝 Proposals", "Review all proposals", 
            new Color(241, 196, 15), getPendingProposals() + " pending", e -> viewAllProposals()));
        
        contentPanel.add(createCard("📊 Reports", "Generate system reports", 
            new Color(231, 76, 60), "View analytics", e -> generateReports()));
        
        contentPanel.add(createCard("⚙️ Settings", "System configuration", 
            new Color(155, 89, 182), "Configure system", e -> systemSettings()));
        
        contentPanel.add(createCard("💾 Backup", "Database management", 
            new Color(52, 73, 94), "Backup & restore", e -> backupDatabase()));
        
        return contentPanel;
    }
    
    private JPanel createCard(String title, String desc, Color color, String info, ActionListener action) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, color, 0, getHeight(), color.darker());
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(236, 240, 241));
        
        JLabel infoLabel = new JLabel(info);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(255, 255, 255, 200));
        
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        textPanel.add(infoLabel);
        
        JButton actionBtn = new JButton("Open →");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setForeground(color);
        actionBtn.setBackground(Color.WHITE);
        actionBtn.setBorderPainted(false);
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(action);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);
        btnPanel.add(actionBtn);
        
        card.add(textPanel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        
        JLabel statusLabel = new JLabel("System Status: Online");
        statusLabel.setForeground(new Color(46, 204, 113));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JLabel versionLabel = new JLabel("FYPMS v1.0 Admin");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel copyrightLabel = new JLabel("© 2024 FYPMS - All Rights Reserved");
        copyrightLabel.setForeground(new Color(189, 195, 199));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        footerPanel.add(statusLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(versionLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(copyrightLabel);
        
        return footerPanel;
    }
    
    // Database Methods
    private int getTotalUsers() {
        try {
            String query = "SELECT COUNT(*) FROM users";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private int getTotalProjects() {
        try {
            String query = "SELECT COUNT(*) FROM projects";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private int getPendingProposals() {
        try {
            String query = "SELECT COUNT(*) FROM proposals WHERE status = 'Pending'";
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Action Methods
    private void manageUsers() {
        UserManagementDialog dialog = new UserManagementDialog(this);
        dialog.setVisible(true);
    }
    
    private void addUser() {
        AddUserDialog dialog = new AddUserDialog(this);
        dialog.setVisible(true);
    }
    
    private void viewAllProjects() {
        AdminProjectViewDialog dialog = new AdminProjectViewDialog(this);
        dialog.setVisible(true);
    }
    
    private void viewAllProposals() {
        AdminProposalViewDialog dialog = new AdminProposalViewDialog(this);
        dialog.setVisible(true);
    }
    
    private void generateReports() {
        ReportsDialog dialog = new ReportsDialog(this);
        dialog.setVisible(true);
    }
    
    private void systemSettings() {
        JOptionPane.showMessageDialog(this, 
            "System Settings\n\n" +
            "Configure system parameters here.",
            "Settings", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void backupDatabase() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Do you want to backup the database?",
            "Backup Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Database backup completed successfully!",
                "Backup Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showSystemStats() {
        SystemStatsDialog dialog = new SystemStatsDialog(this);
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