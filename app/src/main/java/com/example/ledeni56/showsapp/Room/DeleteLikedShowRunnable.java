package com.example.ledeni56.showsapp.Room;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.LikedShow;

public class DeleteLikedShowRunnable implements Runnable {
    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final LikedShow likedShow;

    public DeleteLikedShowRunnable(Context context, LikedShow likedShow, DatabaseCallback<Void> callback) {
        this.context = context;
        this.likedShow = likedShow;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            RoomDatabaseFactory.db(context).showsAppDao().deleteLiked(likedShow);
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(null);
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
