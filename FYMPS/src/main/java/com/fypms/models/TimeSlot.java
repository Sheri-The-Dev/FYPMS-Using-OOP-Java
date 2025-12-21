/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

import java.sql.Time;
import java.util.Date;

/**
 * TimeSlot class - Represents available time slots
 */
public class TimeSlot {
    private int slotID;
    private Date date;
    private Time startTime;
    private boolean isAvailable;
    private int presentationID;
    
    public TimeSlot() {
        this.isAvailable = true;
    }
    
    public TimeSlot(int slotID, Date date, Time startTime) {
        this.slotID = slotID;
        this.date = date;
        this.startTime = startTime;
        this.isAvailable = true;
    }
    
    public void reserve() {
        this.isAvailable = false;
        System.out.println("Time slot reserved");
    }
    
    public void release() {
        this.isAvailable = true;
        System.out.println("Time slot released");
    }
    
    // Getters and Setters
    public int getSlotID() { 
        return slotID; 
    }
    
    public void setSlotID(int slotID) { 
        this.slotID = slotID; 
    }
    
    public Date getDate() { 
        return date; 
    }
    
    public void setDate(Date date) { 
        this.date = date; 
    }
    
    public Time getStartTime() { 
        return startTime; 
    }
    
    public void setStartTime(Time startTime) { 
        this.startTime = startTime; 
    }
    
    public boolean isAvailable() { 
        return isAvailable; 
    }
    
    public void setAvailable(boolean isAvailable) { 
        this.isAvailable = isAvailable; 
    }
    
    public int getPresentationID() { 
        return presentationID; 
    }
    
    public void setPresentationID(int presentationID) { 
        this.presentationID = presentationID; 
    }
}