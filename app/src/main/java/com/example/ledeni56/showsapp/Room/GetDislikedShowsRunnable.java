package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.DislikedShow;
import com.example.ledeni56.showsapp.Entities.LikedShow;

import java.util.List;

public class GetDislikedShowsRunnable implements Runnable{
    private final DatabaseCallback<List<DislikedShow>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetDislikedShowsRunnable(Context context, DatabaseCallback<List<DislikedShow>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<DislikedShow> dislikedShows = RoomDatabaseFactory.db(context).showsAppDao().getDislikedShows();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(dislikedShows);
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
