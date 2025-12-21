/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fypms.models;

/**
 * Abstract User class - Base class for all user types
 */
public abstract class User {
    protected int userID;
    protected String username;
    protected String password;
    protected String email;
    protected String name;
    protected String department;
    
    // Constructors
    public User() {}
    
    public User(int userID, String username, String password, String email, String name, String department) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.department = department;
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract boolean login();
    public abstract void logout();
    public abstract void updateProfile();
    
    // Getters and Setters
    public int getUserID() { 
        return userID; 
    }
    
    public void setUserID(int userID) { 
        this.userID = userID; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }
    
    public String getDepartment() { 
        return department; 
    }
    
    public void setDepartment(String department) { 
        this.department = department; 
    }
}