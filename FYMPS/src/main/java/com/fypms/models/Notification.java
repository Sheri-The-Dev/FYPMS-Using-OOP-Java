/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

import java.util.Date;

/**
 * Notification class - Represents system notifications
 */
public class Notification {
    private int notificationID;
    private int userID;
    private String message;
    private String type;
    private boolean isRead;
    private Date createdAt;
    
    public Notification() {
        this.isRead = false;
        this.createdAt = new Date();
    }
    
    public Notification(int notificationID, int userID, String message, String type) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.createdAt = new Date();
    }
    
    public void send() {
        // Implementation
        System.out.println("Notification sent");
    }
    
    public void markAsRead() {
        this.isRead = true;
        System.out.println("Notification marked as read");
    }
    
    // Getters and Setters
    public int getNotificationID() { 
        return notificationID; 
    }
    
    public void setNotificationID(int notificationID) { 
        this.notificationID = notificationID; 
    }
    
    public int getUserID() { 
        return userID; 
    }
    
    public void setUserID(int userID) { 
        this.userID = userID; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    public void setMessage(String message) { 
        this.message = message; 
    }
    
    public String getType() { 
        return type; 
    }
    
    public void setType(String type) { 
        this.type = type; 
    }
    
    public boolean isRead() { 
        return isRead; 
    }
    
    public void setRead(boolean isRead) { 
        this.isRead = isRead; 
    }
    
    public Date getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(Date createdAt) { 
        this.createdAt = createdAt; 
    }
}