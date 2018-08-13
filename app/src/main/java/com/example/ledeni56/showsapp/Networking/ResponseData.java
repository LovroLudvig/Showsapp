package com.example.ledeni56.showsapp.Networking;

import com.squareup.moshi.Json;


public class ResponseData<T> {
    @Json(name = "data")
    private T data;

    public T getData() {
        return data;
    }
}
