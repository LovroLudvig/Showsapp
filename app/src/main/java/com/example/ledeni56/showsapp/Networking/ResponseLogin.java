package com.example.ledeni56.showsapp.Networking;

import com.squareup.moshi.Json;


public class ResponseLogin {
    @Json(name = "data")
    private DataLogin data;

    public DataLogin getData() {
        return data;
    }

    public static class DataLogin {
        @Json(name = "token")
        private String token;

        public String getToken() {
            return token;
        }
    }
}
