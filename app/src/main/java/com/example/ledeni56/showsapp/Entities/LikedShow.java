package com.example.ledeni56.showsapp.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class LikedShow {
    @PrimaryKey
    @NonNull
    private String id;

    public LikedShow(String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }
}
