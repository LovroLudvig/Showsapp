package com.example.ledeni56.showsapp;

import android.provider.ContactsContract;

import com.squareup.moshi.Json;

/**
 * Created by lovro on 7/25/2018.
 */

public class ResponseRegister {
    @Json(name = "data")
    private DataRegister data;

    public DataRegister getData() {
        return data;
    }

    public static class DataRegister {
        @Json(name = "type")
        private String type;
        @Json(name = "email")
        private String email;
        @Json(name = "_id")
        private String id;

        public String getEmail() {
            return email;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }
}
