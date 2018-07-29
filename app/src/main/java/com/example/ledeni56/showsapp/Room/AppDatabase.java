package com.example.ledeni56.showsapp.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;


@Database(
        entities = {
                Show.class,
                Episode.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShowsAppDao showsAppDao();
}