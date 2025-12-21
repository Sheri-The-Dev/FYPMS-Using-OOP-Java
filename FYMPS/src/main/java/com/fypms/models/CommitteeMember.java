/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

/**
 * CommitteeMember class - Represents a committee member
 */
public class CommitteeMember extends User {
    private String committeeID;
    
    public CommitteeMember() {
        super();
    }
    
    public CommitteeMember(int userID, String username, String password, String email, 
                          String name, String department, String committeeID) {
        super(userID, username, password, email, name, department);
        this.committeeID = committeeID;
    }
    
    @Override
    public boolean login() {
        return true;
    }
    
    @Override
    public void logout() {
        System.out.println("Committee member logged out");
    }
    
    @Override
    public void updateProfile() {
        System.out.println("Committee member profile updated");
    }
    
    public void evaluateProject(Project project) {
        // Implementation
        System.out.println("Project evaluated by committee member");
    }
    
    public void provideFinalMarks(Project project, float marks) {
        // Implementation
        System.out.println("Final marks provided");
    }
    
    public String getCommitteeID() { 
        return committeeID; 
    }
    
    public void setCommitteeID(String committeeID) { 
        this.committeeID = committeeID; 
    }

    private static class Project {

        public Project() {
        }
    }
}