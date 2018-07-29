package com.example.ledeni56.showsapp.Interfaces;

import com.example.ledeni56.showsapp.Networking.ApiEpisode;
import com.example.ledeni56.showsapp.Networking.ApiShowDescription;
import com.example.ledeni56.showsapp.Networking.ApiShowId;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.Networking.ResponseLogin;
import com.example.ledeni56.showsapp.Networking.ResponseRegister;
import com.example.ledeni56.showsapp.Networking.UserLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/api/users/sessions")
    Call<ResponseLogin> login(@Body UserLogin userLogin);

    @POST("/api/users")
    Call<ResponseRegister> register(@Body UserLogin userLogin);

    @GET("/api/shows")
    Call<ResponseData<List<ApiShowId>>> getShowIds();

    @GET("/api/shows/{showId}")
    Call<ResponseData<ApiShowDescription>> getShows(@Path("showId") String id);

    @GET("/api/shows/{showId}/episodes")
    Call<ResponseData<List<ApiEpisode>>> getEpisodes(@Path("showId") String id);
}
