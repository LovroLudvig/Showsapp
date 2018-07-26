package com.example.ledeni56.showsapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/users/sessions")
    Call<ResponseLogin> login(@Body UserLogin userLogin);

    @POST("/api/users")
    Call<ResponseRegister> register(@Body UserLogin userLogin);
}
