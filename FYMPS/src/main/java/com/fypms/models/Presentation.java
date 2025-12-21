/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

import java.sql.Time;
import java.util.Date;

/**
 * Presentation class - Represents project presentations
 */
public class Presentation {
    private int presentationID;
    private Date date;
    private Time time;
    private String venue;
    private String status;
    
    public Presentation() {
        this.status = "Scheduled";
    }
    
    public Presentation(int presentationID, Date date, Time time, String venue) {
        this.presentationID = presentationID;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.status = "Scheduled";
    }
    
    public void schedule() {
        // Implementation
        this.status = "Scheduled";
        System.out.println("Presentation scheduled");
    }
    
    public void complete() {
        this.status = "Completed";
        System.out.println("Presentation completed");
    }
    
    public void cancel() {
        this.status = "Cancelled";
        System.out.println("Presentation cancelled");
    }
    
    // Getters and Setters
    public int getPresentationID() { 
        return presentationID; 
    }
    
    public void setPresentationID(int presentationID) { 
        this.presentationID = presentationID; 
    }
    
    public Date getDate() { 
        return date; 
    }
    
    public void setDate(Date date) { 
        this.date = date; 
    }
    
    public Time getTime() { 
        return time; 
    }
    
    public void setTime(Time time) { 
        this.time = time; 
    }
    
    public String getVenue() { 
        return venue; 
    }
    
    public void setVenue(String venue) { 
        this.venue = venue; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
}
