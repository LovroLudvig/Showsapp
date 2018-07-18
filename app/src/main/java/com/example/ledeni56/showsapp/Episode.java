package com.example.ledeni56.showsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable{
    private String name;
    private String description;

    //my part:
    public Episode(String name, String description){
        this.name=name;
        this.description=description;
    }


    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }



    //Parcelable part:
    protected Episode(Parcel in) {
        name = in.readString();
        description = in.readString();
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
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Episode) {
            return this.name.equals(((Episode) obj).getName());
        }else{
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }
}
