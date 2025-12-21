/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import com.fypms.database.DatabaseConnection;
import java.sql.*;

public class DocumentListDialog extends JDialog {
    
    private int userID;
    private JTable documentTable;
    private DefaultTableModel tableModel;
    
    public DocumentListDialog(JFrame parent, int userID) {
        super(parent, "My Documents", true);
        this.userID = userID;
        initComponents();
        loadDocuments();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel headerLabel = new JLabel("📄 My Documents");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // Table
        String[] columns = {"ID", "Title", "Project", "File Type", "Upload Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        documentTable = new JTable(tableModel);
        documentTable.setRowHeight(35);
        documentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        documentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        documentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        documentTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(documentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton viewBtn = new JButton("👁️ View");
        viewBtn.setPreferredSize(new Dimension(100, 40));
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        viewBtn.setBorderPainted(false);
        viewBtn.setFocusPainted(false);
        viewBtn.addActionListener(e -> viewDocument());
        
        JButton downloadBtn = new JButton("⬇️ Download");
        downloadBtn.setPreferredSize(new Dimension(140, 40));
        downloadBtn.setBackground(new Color(46, 204, 113));
        downloadBtn.setForeground(Color.WHITE);
        downloadBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        downloadBtn.setBorderPainted(false);
        downloadBtn.setFocusPainted(false);
        downloadBtn.addActionListener(e -> downloadDocument());
        
        JButton deleteBtn = new JButton("🗑️ Delete");
        deleteBtn.setPreferredSize(new Dimension(100, 40));
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteBtn.setBorderPainted(false);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteDocument());
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.setBackground(new Color(241, 196, 15));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadDocuments());
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(100, 40));
        closeBtn.setBackground(new Color(149, 165, 166));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(viewBtn);
        buttonPanel.add(downloadBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadDocuments() {
        tableModel.setRowCount(0);
        
        try {
            String query = "SELECT d.documentID, d.title, p.title as projectTitle, d.fileType, d.uploadDate " +
                          "FROM documents d " +
                          "JOIN projects p ON d.projectID = p.projectID " +
                          "WHERE d.uploadedBy = ? " +
                          "ORDER BY d.uploadDate DESC";
            
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("documentID"),
                    rs.getString("title"),
                    rs.getString("projectTitle"),
                    rs.getString("fileType"),
                    rs.getDate("uploadDate")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading documents: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewDocument() {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int docID = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            String query = "SELECT * FROM documents WHERE documentID = ?";
            PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
            pst.setInt(1, docID);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String info = String.format(
                    "Document ID: %d\n" +
                    "Title: %s\n" +
                    "File Type: %s\n" +
                    "Upload Date: %s\n" +
                    "File Path: %s",
                    rs.getInt("documentID"),
                    rs.getString("title"),
                    rs.getString("fileType"),
                    rs.getDate("uploadDate"),
                    rs.getString("filePath")
                );
                
                JOptionPane.showMessageDialog(this, info, "Document Details", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void downloadDocument() {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Download functionality would open the file in the default application.\n" +
            "In a real system, this would copy the file to a selected location.",
            "Download",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteDocument() {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int docID = (int) tableModel.getValueAt(selectedRow, 0);
        String title = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete document: " + title + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "DELETE FROM documents WHERE documentID = ?";
                PreparedStatement pst = DatabaseConnection.getInstance().prepareStatement(query);
                pst.setInt(1, docID);
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Document deleted!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDocuments();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}