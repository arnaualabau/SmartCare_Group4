package com.example.smartcare_group4.data.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String imageUrl;
    private String email;
    private String password;
    private String error;

    public UserItem() {
    }

    public UserItem(String name, String imageUrl, String email, String password) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserItem{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
