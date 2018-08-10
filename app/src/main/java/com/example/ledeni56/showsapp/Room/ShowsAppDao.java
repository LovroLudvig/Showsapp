package com.example.ledeni56.showsapp.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.DislikedShow;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.LikedShow;
import com.example.ledeni56.showsapp.Entities.Show;

import java.util.List;

@Dao
public interface ShowsAppDao {

    @Query("SELECT * FROM show")
    List<Show> getShows();

    @Query("SELECT * FROM comment")
    List<Comment> getComments();

    @Query("SELECT * FROM likedshow")
    List<LikedShow> getLikedShows();

    @Query("SELECT * FROM dislikedshow")
    List<DislikedShow> getDislikedShows();

    @Query("SELECT * FROM episode")
    List<Episode> getEpisodes();

    @Delete
    void deleteDisliked(DislikedShow dislikedShow);

    @Delete
    void deleteLiked(LikedShow likedShow);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShows(List<Show> shows);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisodes(List<Episode> episodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLikedShow(LikedShow likedShow);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDislikedShow(DislikedShow dislikedShow);

}
