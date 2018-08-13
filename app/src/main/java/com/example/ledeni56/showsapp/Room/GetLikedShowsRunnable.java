package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.LikedShow;

import java.util.List;


public class GetLikedShowsRunnable implements Runnable {
    private final DatabaseCallback<List<LikedShow>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetLikedShowsRunnable(Context context, DatabaseCallback<List<LikedShow>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<LikedShow> likedShows = RoomDatabaseFactory.db(context).showsAppDao().getLikedShows();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(likedShows);
                }
            });
        } catch (final Exception e) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(e);
                }
            });
        }
    }
}
