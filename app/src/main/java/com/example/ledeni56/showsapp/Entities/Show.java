package com.example.ledeni56.showsapp.Entities;



import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class Show {
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @Ignore
    private ArrayList<Episode> episodes= new ArrayList<>();
    @ColumnInfo(name = "pictureUrl")
    private String pictureUrl;
    @ColumnInfo(name = "likesCount")
    private int likesCount;




    public Show(String id, String name,String description, String pictureUrl, int likesCount){
        this.id=id;
        this.name=name;
        this.description=description;
        this.pictureUrl ="https://api.infinum.academy"+pictureUrl;
        this.likesCount=likesCount;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Show) {
            return this.id.equals(((Show) obj).getId());
        }else{
            return false;
        }
    }


    //adds an episode if one doesn't already exist with the same episode number and season number
    public boolean addEpisode(Episode episode){
        if (episodes.contains(episode)){
            return false;
        }
        else{
            episodes.add(episode);
            return true;
        }
    }

}
