package com.example.ledeni56.showsapp;


import com.squareup.moshi.Json;

public class UserLogin {
    @Json(name = "email")
    private String email;
    @Json(name = "password")
    private String password;

    public UserLogin(String mail, String pass){
        email=mail;
        password=pass;
    }
}
