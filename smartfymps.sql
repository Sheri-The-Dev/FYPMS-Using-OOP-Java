-- FYPMS Database Schema
CREATE DATABASE IF NOT EXISTS smartfypms;
USE smartfypms;

-- Users table (parent table)
CREATE TABLE users (
    userID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    department VARCHAR(100),
    userType ENUM('Student', 'Supervisor', 'Administrator', 'CommitteeMember') NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE students (
    studentID VARCHAR(20) PRIMARY KEY,
    userID INT UNIQUE NOT NULL,
    cgpa DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Supervisors table
CREATE TABLE supervisors (
    supervisorID VARCHAR(20) PRIMARY KEY,
    userID INT UNIQUE NOT NULL,
    maxCapacity INT DEFAULT 5,
    currentLoad INT DEFAULT 0,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Administrators table
CREATE TABLE administrators (
    adminID VARCHAR(20) PRIMARY KEY,
    userID INT UNIQUE NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Committee Members table
CREATE TABLE committee_members (
    committeeID VARCHAR(20) PRIMARY KEY,
    userID INT UNIQUE NOT NULL,
    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE
);

-- Groups table
CREATE TABLE groups (
    groupID INT PRIMARY KEY AUTO_INCREMENT,
    groupName VARCHAR(100) NOT NULL,
    maxSize INT DEFAULT 3,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Group membership
CREATE TABLE group_members (
    groupID INT,
    studentID VARCHAR(20),
    joinedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (groupID, studentID),
    FOREIGN KEY (groupID) REFERENCES groups(groupID) ON DELETE CASCADE,
    FOREIGN KEY (studentID) REFERENCES students(studentID) ON DELETE CASCADE
);

-- Projects table
CREATE TABLE projects (
    projectID INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status ENUM('Proposed', 'Approved', 'InProgress', 'Completed', 'Rejected') DEFAULT 'Proposed',
    finalGrade DECIMAL(5,2),
    groupID INT,
    supervisorID VARCHAR(20),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (groupID) REFERENCES groups(groupID) ON DELETE SET NULL,
    FOREIGN KEY (supervisorID) REFERENCES supervisors(supervisorID) ON DELETE SET NULL
);

-- Proposals table
CREATE TABLE proposals (
    proposalID INT PRIMARY KEY AUTO_INCREMENT,
    projectID INT,
    submissionDate DATE NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    objectives TEXT,
    methodology TEXT,
    supervisorID VARCHAR(20),
    submittedBy VARCHAR(20),
    FOREIGN KEY (projectID) REFERENCES projects(projectID) ON DELETE CASCADE,
    FOREIGN KEY (supervisorID) REFERENCES supervisors(supervisorID),
    FOREIGN KEY (submittedBy) REFERENCES students(studentID)
);

-- Documents table
CREATE TABLE documents (
    documentID INT PRIMARY KEY AUTO_INCREMENT,
    projectID INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    filePath VARCHAR(500) NOT NULL,
    fileType VARCHAR(50),
    uploadDate DATE NOT NULL,
    version INT DEFAULT 1,
    uploadedBy INT,
    FOREIGN KEY (projectID) REFERENCES projects(projectID) ON DELETE CASCADE,
    FOREIGN KEY (uploadedBy) REFERENCES users(userID)
);

-- Evaluations table
CREATE TABLE evaluations (
    evaluationID INT PRIMARY KEY AUTO_INCREMENT,
    projectID INT NOT NULL,
    technicalScore DECIMAL(5,2),
    documentationScore DECIMAL(5,2),
    presentationScore DECIMAL(5,2),
    totalScore DECIMAL(5,2),
    evaluatedBy VARCHAR(20),
    evaluationDate DATE,