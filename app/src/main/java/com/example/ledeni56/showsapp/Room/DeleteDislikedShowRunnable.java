package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.DislikedShow;

public class DeleteDislikedShowRunnable implements Runnable {
    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final DislikedShow dislikedShow;

    public DeleteDislikedShowRunnable(Context context, DislikedShow dislikedShow, DatabaseCallback<Void> callback) {
        this.context = context;
        this.dislikedShow = dislikedShow;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            RoomDatabaseFactory.db(context).showsAppDao().deleteDisliked(dislikedShow);
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
