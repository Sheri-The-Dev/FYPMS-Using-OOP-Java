/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import com.fypms.services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProposalApprovalDialog extends JDialog {
    private JTable proposalTable;
    private DefaultTableModel tableModel;
    private ProposalService proposalService;
    private int supervisorID;
    
    public ProposalApprovalDialog(JFrame parent, int supervisorID) {
        super(parent, "Pending Proposals", true);
        this.supervisorID = supervisorID;
        this.proposalService = new ProposalService();
        initComponents();
        loadProposals();
    }
    
    private void initComponents() {
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📋 Pending Proposals for Review");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"ID", "Project Title", "Student", "Submission Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        proposalTable = new JTable(tableModel);
        proposalTable.setRowHeight(35);
        proposalTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        proposalTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        proposalTable.getTableHeader().setBackground(new Color(52, 73, 94));
        proposalTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(proposalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewBtn = new JButton("📄 View Details");
        viewBtn.setPreferredSize(new Dimension(140, 40));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        viewBtn.setBorderPainted(false);
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> viewDetails());
        
        JButton approveBtn = new JButton("✓ Approve");
        approveBtn.setPreferredSize(new Dimension(120, 40));
        approveBtn.setBackground(new Color(46, 204, 113));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        approveBtn.setBorderPainted(false);
        approveBtn.setFocusPainted(false);
        approveBtn.addActionListener(e -> updateProposal("Approved"));
        
        JButton rejectBtn = new JButton("✗ Reject");
        rejectBtn.setPreferredSize(new Dimension(120, 40));
        rejectBtn.setBackground(new Color(231, 76, 60));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rejectBtn.setBorderPainted(false);
        rejectBtn.setFocusPainted(false);
        rejectBtn.addActionListener(e -> updateProposal("Rejected"));
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadProposals() {
        tableModel.setRowCount(0);
        
        // Add sample proposals (replace with actual database query)
        tableModel.addRow(new Object[]{1, "AI-Based Traffic Management System", "Alice Johnson", "2024-12-05", "Pending"});
        tableModel.addRow(new Object[]{2, "Smart Healthcare Monitoring", "Bob Smith", "2024-12-06", "Pending"});
        tableModel.addRow(new Object[]{3, "E-Learning Platform", "Charlie Brown", "2024-12-07", "Pending"});
    }
    
    private void viewDetails() {
        int selectedRow = proposalTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String student = (String) tableModel.getValueAt(selectedRow, 2);
            
            JOptionPane.showMessageDialog(this,
                "Project: " + title + "\n" +
                "Student: " + student + "\n\n" +
                "Objectives: [Details would be loaded from database]\n" +
                "Methodology: [Details would be loaded from database]",
                "Proposal Details",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a proposal!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void updateProposal(String status) {
        int selectedRow = proposalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a proposal!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int proposalID = (int) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to " + status.toLowerCase() + " this proposal?",
            "Confirm " + status,
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = proposalService.updateProposalStatus(proposalID, status);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Proposal " + status.toLowerCase() + " successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                loadProposals();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update proposal status.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}