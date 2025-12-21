package com.fypms;

import com.fypms.database.DatabaseConnection;
import com.fypms.gui.LoginFrame;
import javax.swing.*;

/**
 * Main Application Class for FYPMS
 * Entry point of the application
 */
public class FYPMSApplication {
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // FIXED: testConnection() is a static method, no need for instance
        if (DatabaseConnection.testConnection()) {
            System.out.println("✅ Database connection successful!");
            
            // Launch Login GUI
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, 
                "❌ Database connection failed!\n\nPlease check:\n" +
                "1. XAMPP MySQL service is running\n" +
                "2. Database 'fypms_db' exists\n" +
                "3. Database credentials in DatabaseConnection.java are correct",
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}