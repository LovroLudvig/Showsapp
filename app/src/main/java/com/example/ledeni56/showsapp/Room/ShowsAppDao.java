package com.example.ledeni56.showsapp.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.ledeni56.showsapp.Entities.DatabaseShow;
import com.example.ledeni56.showsapp.Entities.Episode;

import java.util.List;

@Dao
public interface ShowsAppDao {

    @Query("SELECT * FROM databaseShow")
    List<DatabaseShow> getShows();

    @Query("SELECT * FROM episode")
    List<Episode> getEpisodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShows(List<DatabaseShow> shows);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisodes(List<Episode> episodes);
}
