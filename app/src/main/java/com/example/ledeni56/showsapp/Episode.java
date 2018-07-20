package com.example.ledeni56.showsapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable{
    private String name;
    private String description;

    private int episodeNumber;
    private int seasonNumber;

    private Uri picture;

    public Episode(String name, String description, int ep, int se, Uri pic){
        this.name=name;
        this.description=description;
        this.episodeNumber=ep;
        this.seasonNumber=se;
        this.picture=pic;
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
        dest.writeParcelable(picture, flags);
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

    public Uri getPicture() {
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
