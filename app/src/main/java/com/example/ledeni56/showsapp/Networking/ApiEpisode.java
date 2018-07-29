package com.example.ledeni56.showsapp.Networking;

import android.net.Uri;

import com.squareup.moshi.Json;

public class ApiEpisode {
    @Json(name = "_id")
    private String id;
    @Json(name = "title")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "episodeNumber")
    private String episodeNumber;
    @Json(name = "season")
    private String seasonNumber;
    @Json(name = "imageUrl")
    private String picture;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public String getPicture() {
        return picture;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }
}
