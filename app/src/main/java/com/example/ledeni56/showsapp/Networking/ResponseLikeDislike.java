package com.example.ledeni56.showsapp.Networking;

import com.squareup.moshi.Json;

/**
 * Created by lovro on 8/6/2018.
 */

public class ResponseLikeDislike {
    @Json(name = "likesCount")
    private int likesCount;

    public int getLikesCount() {
        return likesCount;
    }
}
