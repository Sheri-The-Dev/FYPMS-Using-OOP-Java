/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.fypms.models;

import java.util.Date;

/**
 * Feedback class - Represents feedback on projects
 */
public class Feedback {
    private int feedbackID;
    private String text;
    private int projectID;
    private int evaluatorID;
    private Date createdAt;
    
    public Feedback() {
        this.createdAt = new Date();
    }
    
    public Feedback(int feedbackID, String text, int projectID, int evaluatorID) {
        this.feedbackID = feedbackID;
        this.text = text;
        this.projectID = projectID;
        this.evaluatorID = evaluatorID;
        this.createdAt = new Date();
    }
    
    public void provide() {
        // Implementation
        System.out.println("Feedback provided");
    }
    
    // Getters and Setters
    public int getFeedbackID() { 
        return feedbackID; 
    }
    
    public void setFeedbackID(int feedbackID) { 
        this.feedbackID = feedbackID; 
    }
    
    public String getText() { 
        return text; 
    }
    
    public void setText(String text) { 
        this.text = text; 
    }
    
    public int getProjectID() { 
        return projectID; 
    }
    
    public void setProjectID(int projectID) { 
        this.projectID = projectID; 
    }
    
    public int getEvaluatorID() { 
        return evaluatorID; 
    }
    
    public void setEvaluatorID(int evaluatorID) { 
        this.evaluatorID = evaluatorID; 
    }
    
    public Date getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(Date createdAt) { 
        this.createdAt = createdAt; 
    }
}
