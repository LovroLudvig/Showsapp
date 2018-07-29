package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.DatabaseShow;
import com.example.ledeni56.showsapp.Entities.Show;

import java.util.List;


public class GetShowsRunnable implements Runnable {

    private final DatabaseCallback<List<DatabaseShow>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetShowsRunnable(Context context, DatabaseCallback<List<DatabaseShow>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<DatabaseShow> shows = RoomDatabaseFactory.db(context).showsAppDao().getShows();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(shows);
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
