package com.example.ledeni56.showsapp.Networking;

import com.squareup.moshi.Json;

public class CommentPost {
    @Json(name = "text")
    private String text;
    @Json(name = "episodeId")
    private String episodeId;

    public CommentPost(String text, String episodeId){
        this.text=text;
        this.episodeId=episodeId;
    }
}
