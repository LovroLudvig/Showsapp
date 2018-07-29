package com.example.ledeni56.showsapp.Room;

import android.arch.persistence.room.Room;
import android.content.Context;

public class RoomDatabaseFactory {

    private static AppDatabase database = null;

    private RoomDatabaseFactory() {
    }

    public static AppDatabase db(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "showsApp-database").build();
        }
        return database;
    }
}
