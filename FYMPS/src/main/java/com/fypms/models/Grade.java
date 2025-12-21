/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

/**
 * Grade class - Represents student grades
 */
public class Grade {
    private int gradeID;
    private float proposalMarks;
    private float taskMarks;
    private float presentationMarks;
    private String letterGrade;
    
    public Grade() {}
    
    public Grade(int gradeID, float proposalMarks, float taskMarks, float presentationMarks) {
        this.gradeID = gradeID;
        this.proposalMarks = proposalMarks;
        this.taskMarks = taskMarks;
        this.presentationMarks = presentationMarks;
        calculateFinal();
    }
    
    public void calculateFinal() {
        float total = proposalMarks + taskMarks + presentationMarks;
        if (total >= 90) 
            letterGrade = "A+";
        else if (total >= 85) 
            letterGrade = "A";
        else if (total >= 80) 
            letterGrade = "A-";
        else if (total >= 75) 
            letterGrade = "B+";
        else if (total >= 70) 
            letterGrade = "B";
        else if (total >= 65) 
            letterGrade = "B-";
        else if (total >= 60) 
            letterGrade = "C+";
        else if (total >= 55) 
            letterGrade = "C";
        else if (total >= 50) 
            letterGrade = "C-";
        else 
            letterGrade = "F";
    }
    
    public void approve() {
        // Implementation
        System.out.println("Grade approved");
    }
    
    // Getters and Setters
    public int getGradeID() { 
        return gradeID; 
    }
    
    public void setGradeID(int gradeID) { 
        this.gradeID = gradeID; 
    }
    
    public float getProposalMarks() { 
        return proposalMarks; 
    }
    
    public void setProposalMarks(float proposalMarks) { 
        this.proposalMarks = proposalMarks;
        calculateFinal();
    }
    
    public float getTaskMarks() { 
        return taskMarks; 
    }
    
    public void setTaskMarks(float taskMarks) { 
        this.taskMarks = taskMarks;
        calculateFinal();
    }
    
    public float getPresentationMarks() { 
        return presentationMarks; 
    }
    
    public void setPresentationMarks(float presentationMarks) { 
        this.presentationMarks = presentationMarks;
        calculateFinal();
    }
    
    public String getLetterGrade() { 
        return letterGrade; 
    }
    
    public void setLetterGrade(String letterGrade) { 
        this.letterGrade = letterGrade; 
    }
}