package com.example.ledeni56.showsapp.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DatabaseShow {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "pictureUrl")
    private String pictureUrl;

    public DatabaseShow(String id, String name, String description, String pictureUrl){
        this.id=id;
        this.name=name;
        this.description=description;
        this.pictureUrl=pictureUrl;

    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
