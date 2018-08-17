package com.example.ledeni56.showsapp.Entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;


@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    @Json(name = "_id")
    private String id;
    @ColumnInfo(name = "text")
    @Json(name = "text")
    private String text;
    @ColumnInfo(name = "episodeId")
    @Json(name = "episodeId")
    private String episodeId;
    @ColumnInfo(name = "userEmail")
    @Json(name = "userEmail")
    private String userEmail;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
