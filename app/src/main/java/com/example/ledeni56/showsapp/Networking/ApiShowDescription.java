package com.example.ledeni56.showsapp.Networking;

import com.squareup.moshi.Json;

public class ApiShowDescription {
    @Json(name = "_id")
    private String id;
    @Json(name = "title")
    private String name;
    @Json(name = "description")
    private String description;
    @Json(name = "imageUrl")
    private String pictureUrl;
    @Json(name = "likesCount")
    private int likesCOunt;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getLikesCOunt() {
        return likesCOunt;
    }
}
