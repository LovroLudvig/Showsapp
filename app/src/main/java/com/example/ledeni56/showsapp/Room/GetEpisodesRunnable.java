package com.example.ledeni56.showsapp.Room;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.Episode;


import java.util.List;

public class GetEpisodesRunnable implements Runnable {

    private final DatabaseCallback<List<Episode>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetEpisodesRunnable(Context context, DatabaseCallback<List<Episode>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<Episode> episodes = RoomDatabaseFactory.db(context).showsAppDao().getEpisodes();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(episodes);
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