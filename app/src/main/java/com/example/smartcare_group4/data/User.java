package com.example.smartcare_group4.data;

public class User {

    public String username;
    public String email;
    public boolean patient;
    public String hardwareId;
    public boolean imageTaken;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String hardwareId, boolean patient, boolean imageTaken) {
        this.username = username;
        this.email = email;
        this.patient = patient;
        this.hardwareId = hardwareId;
        this.imageTaken = imageTaken;
    }

    public boolean isImageTaken() {
        return imageTaken;
    }

    public void setImageTaken(boolean imageTaken) {
        this.imageTaken = imageTaken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPatient() {
        return patient;
    }

    public void setPatient(boolean patient) {
        this.patient = patient;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }
}
