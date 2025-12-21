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

public class AdminProposalViewDialog extends JDialog {
    
    private JTable proposalTable;
    private DefaultTableModel tableModel;
    
    public AdminProposalViewDialog(JFrame parent) {
        super(parent, "All Proposals", true);
        initComponents();
        loadProposals();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📝 All Proposals");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        String[] columns = {"ID", "Project Title", "Student", "Supervisor", "Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        proposalTable = new JTable(tableModel);
        proposalTable.setRowHeight(35);
        proposalTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        proposalTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        proposalTable.getTableHeader().setBackground(new Color(52, 73, 94));
        proposalTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(proposalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewBtn = new JButton("👁️ View Details");
        styleButton(viewBtn, new Color(52, 152, 219));
        viewBtn.addActionListener(e -> viewDetails());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        styleButton(refreshBtn, new Color(46, 204, 113));
        refreshBtn.addActionListener(e -> loadProposals());
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(149, 165, 166));
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void styleButton(JButton btn, Color color) {
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }
    
    private void loadProposals() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT pr.proposalID, p.title, student.name as studentName, " +
                          "supervisor.name as supervisorName, pr.status, pr.submissionDate " +
                          "FROM proposals pr " +
                          "JOIN projects p ON pr.projectID = p.projectID " +
                          "LEFT JOIN students s ON pr.submittedBy = s.studentID " +
                          "LEFT JOIN users student ON s.userID = student.userID " +
                          "LEFT JOIN supervisors sv ON pr.supervisorID = sv.supervisorID " +
                          "LEFT JOIN users supervisor ON sv.userID = supervisor.userID " +
                          "ORDER BY pr.proposalID DESC";
            
            Statement st = DatabaseConnection.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("proposalID"),
                    rs.getString("title"),
                    rs.getString("studentName") != null ? rs.getString("studentName") : "N/A",
                    rs.getString("supervisorName") != null ? rs.getString("supervisorName") : "N/A",
                    rs.getString("status"),
                    rs.getDate("submissionDate")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewDetails() {
        int selectedRow = proposalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a proposal!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int proposalID = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String query = "SELECT pr.*, p.title FROM proposals pr " +
                          "JOIN projects p ON pr.projectID = p.projectID WHERE pr.proposalID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, proposalID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String details = String.format(
                    "Proposal ID: %d\nProject: %s\n\nObjectives:\n%s\n\nMethodology:\n%s\n\nStatus: %s\nDate: %s",
                    rs.getInt("proposalID"),
                    rs.getString("title"),
                    rs.getString("objectives"),
                    rs.getString("methodology"),
                    rs.getString("status"),
                    rs.getDate("submissionDate")
                );
                
                JOptionPane.showMessageDialog(this, details, "Proposal Details", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

