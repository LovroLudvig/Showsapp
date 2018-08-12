package com.example.ledeni56.showsapp.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.squareup.moshi.Json;

@Entity
public class Episode implements Parcelable{
    @PrimaryKey
    @NonNull
    private String id;
    @ColumnInfo(name = "ownerId")
    private String ownerId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "episodeNumber")
    private int episodeNumber;
    @ColumnInfo(name = "seasonNumber")
    private int seasonNumber;
    @ColumnInfo(name = "picture")
    private String picture;

    public Episode(String id,String ownerId, String name, String description, int episodeNumber, int seasonNumber, String picture){
        this.id=id;
        this.ownerId=ownerId;
        this.name=name;
        this.description=description;
        this.episodeNumber=episodeNumber;
        this.seasonNumber=seasonNumber;
        this.picture=picture;
    }
    public Episode(String id,String ownerId, String name, String description, String episodeNumber, String seasonNumber, String picture){
        this.id=id;
        this.ownerId=ownerId;
        this.name=name;
        this.description=description;
        this.episodeNumber=Integer.valueOf(episodeNumber);
        this.seasonNumber=Integer.valueOf(seasonNumber);
        this.picture="https://api.infinum.academy"+picture;
    }



    protected Episode(Parcel in) {
        name = in.readString();
        description = in.readString();
        episodeNumber = in.readInt();
        seasonNumber = in.readInt();
        picture = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Episode){
            return this.episodeNumber==((Episode) obj).getEpisodeNumber() && this.seasonNumber==((Episode) obj).getSeasonNumber();
        } else{
            return false;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(episodeNumber);
        dest.writeInt(seasonNumber);
        dest.writeString(picture);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public String getOwnerId() {
        return ownerId;
    }

    public String getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }


    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }
}
