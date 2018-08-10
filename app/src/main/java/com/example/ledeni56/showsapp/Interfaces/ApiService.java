package com.example.ledeni56.showsapp.Interfaces;

import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Networking.ApiEpisode;
import com.example.ledeni56.showsapp.Networking.ApiShowDescription;
import com.example.ledeni56.showsapp.Networking.ApiShowId;
import com.example.ledeni56.showsapp.Networking.CommentPost;
import com.example.ledeni56.showsapp.Networking.EpisodeUpload;
import com.example.ledeni56.showsapp.Networking.MediaResponse;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.Networking.ResponseLikeDislike;
import com.example.ledeni56.showsapp.Networking.ResponseLogin;
import com.example.ledeni56.showsapp.Networking.ResponseRegister;
import com.example.ledeni56.showsapp.Networking.UserLogin;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/api/users/sessions")
    Call<ResponseLogin> login(@Body UserLogin userLogin);

    @POST("/api/comments")
    Call<ResponseData<Comment>> postComment(@Header ("Authorization") String token, @Body CommentPost commentPost);

    @POST("/api/users")
    Call<ResponseRegister> register(@Body UserLogin userLogin);

    @POST("/api/shows/{showId}/like")
    Call<ResponseLikeDislike> likeShow(@Header ("Authorization") String token, @Path("showId") String id);

    @POST("/api/episodes")
    Call<ResponseData<ApiEpisode>> uploadEpisode(@Header ("Authorization") String token, @Body EpisodeUpload episodeUpload);

    @POST("/api/media")
    @Multipart
    Call<ResponseData<MediaResponse>> uploadMedia(@Header("Authorization") String token, @Part("file\"; filename=\"image.jpg\"") RequestBody request);

    @POST("/api/shows/{showId}/dislike")
    Call<ResponseLikeDislike> dislikeShow(@Header ("Authorization") String token, @Path("showId") String id);

    @GET("/api/shows")
    Call<ResponseData<List<ApiShowId>>> getShowIds();

    @GET("/api/shows/{showId}")
    Call<ResponseData<ApiShowDescription>> getShows(@Path("showId") String id);

    @GET("/api/shows/{showId}/episodes")
    Call<ResponseData<List<ApiEpisode>>> getEpisodes(@Path("showId") String id);


    @GET("/api/episodes/{episodeId}/comments")
    Call<ResponseData<List<Comment>>> getComments(@Path("episodeId") String id);
}
