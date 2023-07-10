package com.example.placementcell;

public class User {
    public String fullName,username,email;
    public User(){

    }
    public User(String fullName, String age, String email){
        this.fullName=fullName;
        this.username=username;
        this.email=email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String age) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
