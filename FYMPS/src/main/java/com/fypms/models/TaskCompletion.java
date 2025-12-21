/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

import java.util.Date;

/**
 * TaskCompletion class - Represents project tasks
 */
public class TaskCompletion {
    private int taskID;
    private String taskName;
    private Date deadline;
    private String status;
    private float weightage;
    private int projectID;
    
    public TaskCompletion() {
        this.status = "Pending";
    }
    
    public TaskCompletion(int taskID, String taskName, Date deadline, float weightage) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.deadline = deadline;
        this.weightage = weightage;
        this.status = "Pending";
    }
    
    public void markComplete() {
        this.status = "Completed";
        System.out.println("Task marked as completed");
    }
    
    public void markInProgress() {
        this.status = "InProgress";
        System.out.println("Task marked as in progress");
    }
    
    public void approve() {
        // Implementation
        System.out.println("Task approved");
    }
    
    // Getters and Setters
    public int getTaskID() { 
        return taskID; 
    }
    
    public void setTaskID(int taskID) { 
        this.taskID = taskID; 
    }
    
    public String getTaskName() { 
        return taskName; 
    }
    
    public void setTaskName(String taskName) { 
        this.taskName = taskName; 
    }
    
    public Date getDeadline() { 
        return deadline; 
    }
    
    public void setDeadline(Date deadline) { 
        this.deadline = deadline; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public float getWeightage() { 
        return weightage; 
    }
    
    public void setWeightage(float weightage) { 
        this.weightage = weightage; 
    }
    
    public int getProjectID() { 
        return projectID; 
    }
    
    public void setProjectID(int projectID) { 
        this.projectID = projectID; 
    }
}
