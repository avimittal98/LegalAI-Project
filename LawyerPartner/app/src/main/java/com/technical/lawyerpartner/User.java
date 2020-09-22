package com.technical.lawyerpartner;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String email;
    public String city;
    public String phone;
    public String Uid;
    public String lawyer;
    public String dob;
    public String request;
    public String photo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String phone, String city, String dob, String Uid, String photo) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.Uid = Uid;
        this.dob = dob;
        this.photo = photo;
        this.request = null;
        this.lawyer = null;
    }

}