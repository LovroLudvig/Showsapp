package com.example.ledeni56.showsapp.Networking;


import com.squareup.moshi.Json;


public class ApiShowId {
    @Json(name = "_id")
    private String id;
    @Json(name = "title")
    private String name;

    @Json(name = "imageUrl")
    private String pictureUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
