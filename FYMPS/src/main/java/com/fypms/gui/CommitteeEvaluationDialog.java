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

public class CommitteeEvaluationDialog extends JDialog {
    
    private int userID;
    private JTable projectTable;
    private DefaultTableModel tableModel;
    
    public CommitteeEvaluationDialog(JFrame parent, int userID) {
        super(parent, "Evaluate Projects", true);
        this.userID = userID;
        initComponents();
        loadProjects();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("⭐ Project Evaluation");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"Project ID", "Title", "Student", "Supervisor", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        projectTable = new JTable(tableModel);
        projectTable.setRowHeight(35);
        projectTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        projectTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        projectTable.getTableHeader().setBackground(new Color(52, 73, 94));
        projectTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(projectTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton evaluateBtn = new JButton("📝 Evaluate");
        evaluateBtn.setPreferredSize(new Dimension(130, 40));
        evaluateBtn.setBackground(new Color(46, 204, 113));
        evaluateBtn.setForeground(Color.WHITE);
        evaluateBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        evaluateBtn.setBorderPainted(false);
        evaluateBtn.setFocusPainted(false);
        evaluateBtn.addActionListener(e -> evaluateProject());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadProjects());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(evaluateBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProjects() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT p.projectID, p.title, u.name as studentName, " +
                          "sup.name as supervisorName, p.status " +
                          "FROM projects p " +
                          "LEFT JOIN groups g ON p.groupID = g.groupID " +
                          "LEFT JOIN group_members gm ON g.groupID = gm.groupID " +
                          "LEFT JOIN students s ON gm.studentID = s.studentID " +
                          "LEFT JOIN users u ON s.userID = u.userID " +
                          "LEFT JOIN supervisors sv ON p.supervisorID = sv.supervisorID " +
                          "LEFT JOIN users sup ON sv.userID = sup.userID " +
                          "WHERE p.status IN ('Approved', 'InProgress') " +
                          "GROUP BY p.projectID";
            
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("projectID"),
                    rs.getString("title"),
                    rs.getString("studentName") != null ? rs.getString("studentName") : "N/A",
                    rs.getString("supervisorName") != null ? rs.getString("supervisorName") : "N/A",
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading projects: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void evaluateProject() {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a project to evaluate!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int projectID = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Create evaluation form
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        JLabel titleLabel = new JLabel("Project: " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(titleLabel);
        panel.add(new JLabel(""));
        
        panel.add(new JLabel("Technical Score (0-100):"));
        JTextField techField = new JTextField("0");
        panel.add(techField);
        
        panel.add(new JLabel("Documentation Score (0-100):"));
        JTextField docField = new JTextField("0");
        panel.add(docField);
        
        panel.add(new JLabel("Presentation Score (0-100):"));
        JTextField presField = new JTextField("0");
        panel.add(presField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Evaluate Project", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                double techScore = Double.parseDouble(techField.getText());
                double docScore = Double.parseDouble(docField.getText());
                double presScore = Double.parseDouble(presField.getText());
                double totalScore = (techScore + docScore + presScore) / 3;
                
                // Get committee ID
                String commitQuery = "SELECT committeeID FROM committee_members WHERE userID = ?";
                PreparedStatement commitPst = DatabaseConnection.getInstance().prepareStatement(commitQuery);
                commitPst.setInt(1, userID);
                ResultSet commitRs = commitPst.executeQuery();
                
                if (commitRs.next()) {
                    String committeeID = commitRs.getString("committeeID");
                    
                    // Insert evaluation
                    String evalQuery = "INSERT INTO evaluations (projectID, technicalScore, documentationScore, " +
                                      "presentationScore, totalScore, evaluatedBy, evaluationDate) " +
                                      "VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
                    PreparedStatement evalPst = DatabaseConnection.getInstance().prepareStatement(evalQuery);
                    evalPst.setInt(1, projectID);
                    evalPst.setDouble(2, techScore);
                    evalPst.setDouble(3, docScore);
                    evalPst.setDouble(4, presScore);
                    evalPst.setDouble(5, totalScore);
                    evalPst.setString(6, committeeID);
                    evalPst.executeUpdate();
                    
                    // Update project status
                    String updateQuery = "UPDATE projects SET status = 'Completed', finalGrade = ? WHERE projectID = ?";
                    PreparedStatement updatePst = DatabaseConnection.getInstance().prepareStatement(updateQuery);
                    updatePst.setDouble(1, totalScore);
                    updatePst.setInt(2, projectID);
                    updatePst.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this,
                        "Evaluation submitted successfully!\nTotal Score: " + String.format("%.2f", totalScore),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    loadProjects();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for scores!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving evaluation: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}