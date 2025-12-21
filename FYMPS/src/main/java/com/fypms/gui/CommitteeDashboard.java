package com.fypms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class CommitteeDashboard extends JFrame {
    
    private int userID;
    private String username;
    
    public CommitteeDashboard(int userID, String username) {
        this.userID = userID;
        this.username = username;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("FYPMS - Committee Member Dashboard");
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
        menuBar.setBackground(new Color(26, 188, 156));
        menuBar.setBorderPainted(false);
        
        JMenu fileMenu = createStyledMenu("File");
        JMenuItem logoutItem = createStyledMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        
        JMenu projectsMenu = createStyledMenu("Projects");
        JMenuItem evaluateItem = createStyledMenuItem("Evaluate Projects");
        evaluateItem.addActionListener(e -> evaluateProjects());
        JMenuItem allProjectsItem = createStyledMenuItem("All Projects");
        allProjectsItem.addActionListener(e -> viewAllProjects());
        projectsMenu.add(evaluateItem);
        projectsMenu.add(allProjectsItem);
        
        JMenu gradesMenu = createStyledMenu("Grades");
        JMenuItem viewGradesItem = createStyledMenuItem("View All Grades");
        viewGradesItem.addActionListener(e -> viewGrades());
        JMenuItem provideFeedbackItem = createStyledMenuItem("Provide Feedback");
        provideFeedbackItem.addActionListener(e -> provideFeedback());
        gradesMenu.add(viewGradesItem);
        gradesMenu.add(provideFeedbackItem);
        
        menuBar.add(fileMenu);
        menuBar.add(projectsMenu);
        menuBar.add(gradesMenu);
        
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
        headerPanel.setBackground(new Color(26, 188, 156));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel("Committee Member Portal");
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
        
        JButton reportsBtn = new JButton("📊 My Reports");
        reportsBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        reportsBtn.setForeground(Color.WHITE);
        reportsBtn.setBackground(new Color(22, 160, 133));
        reportsBtn.setBorderPainted(false);
        reportsBtn.setFocusPainted(false);
        reportsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportsBtn.addActionListener(e -> showReports());
        
        rightPanel.add(dateLabel);
        rightPanel.add(reportsBtn);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(new Color(236, 240, 241));
        
        contentPanel.add(createCard(
            "⭐ Evaluate Projects",
            "Review and grade student projects",
            new Color(52, 152, 219),
            getProjectsToEvaluate() + " projects pending",
            e -> evaluateProjects()
        ));
        
        contentPanel.add(createCard(
            "📁 All Projects",
            "View all submitted projects",
            new Color(46, 204, 113),
            getTotalProjects() + " total projects",
            e -> viewAllProjects()
        ));
        
        contentPanel.add(createCard(
            "📊 View Grades",
            "Review submitted grades",
            new Color(241, 196, 15),
            "Grade overview",
            e -> viewGrades()
        ));
        
        contentPanel.add(createCard(
            "💬 Provide Feedback",
            "Give feedback to students",
            new Color(155, 89, 182),
            "Submit feedback",
            e -> provideFeedback()
        ));
        
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
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(false);
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel descLabel = new JLabel(desc);
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
        
        JLabel roleLabel = new JLabel("Committee Member");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel copyrightLabel = new JLabel("© 2024 FYPMS");
        copyrightLabel.setForeground(new Color(189, 195, 199));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        footerPanel.add(statusLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(roleLabel);
        footerPanel.add(new JLabel("|") {{ setForeground(Color.WHITE); }});
        footerPanel.add(copyrightLabel);
        
        return footerPanel;
    }
    
    // Database Methods
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
    
    private int getProjectsToEvaluate() {
        try {
            String query = "SELECT COUNT(*) FROM projects WHERE status = 'InProgress' " +
                          "AND projectID NOT IN (SELECT projectID FROM evaluations WHERE evaluatedBy = ?)";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            
            // Get committeeID from userID
            String commitQuery = "SELECT committeeID FROM committee_members WHERE userID = ?";
            PreparedStatement commitPst = DatabaseConnection.getInstance().prepareStatement(commitQuery);
            commitPst.setInt(1, userID);
            ResultSet commitRs = commitPst.executeQuery();
            
            if (commitRs.next()) {
                String committeeID = commitRs.getString("committeeID");
                pst.setString(1, committeeID);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Action Methods
    private void evaluateProjects() {
        CommitteeEvaluationDialog dialog = new CommitteeEvaluationDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void viewAllProjects() {
        CommitteeProjectViewDialog dialog = new CommitteeProjectViewDialog(this);
        dialog.setVisible(true);
    }
    
    private void viewGrades() {
        CommitteeGradeViewDialog dialog = new CommitteeGradeViewDialog(this);
        dialog.setVisible(true);
    }
    
    private void provideFeedback() {
        CommitteeFeedbackDialog dialog = new CommitteeFeedbackDialog(this, userID);
        dialog.setVisible(true);
    }
    
    private void showReports() {
        JOptionPane.showMessageDialog(this,
            "Committee Reports\n\n" +
            "Total Projects Evaluated: " + getTotalProjects() + "\n" +
            "Pending Evaluations: " + getProjectsToEvaluate(),
            "My Reports",
            JOptionPane.INFORMATION_MESSAGE);
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