package com.example.ledeni56.showsapp.Interfaces;

import com.example.ledeni56.showsapp.Networking.ResponseLogin;
import com.example.ledeni56.showsapp.Networking.ResponseRegister;
import com.example.ledeni56.showsapp.Networking.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/users/sessions")
    Call<ResponseLogin> login(@Body UserLogin userLogin);

    @POST("/api/users")
    Call<ResponseRegister> register(@Body UserLogin userLogin);
}
