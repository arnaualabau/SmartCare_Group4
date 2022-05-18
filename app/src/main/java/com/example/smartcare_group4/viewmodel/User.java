package com.example.smartcare_group4.viewmodel;

public class User {

    public String username;
    public String email;
    public String password;
    public boolean patient;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password, boolean patient) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.patient = patient;
    }
}
