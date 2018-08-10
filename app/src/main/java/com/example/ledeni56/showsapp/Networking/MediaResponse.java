package com.example.ledeni56.showsapp.Networking;
import com.squareup.moshi.Json;

public class MediaResponse {
    @Json(name = "_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }
}
