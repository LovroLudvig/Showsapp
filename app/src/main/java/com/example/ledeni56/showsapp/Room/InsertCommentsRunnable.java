package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.Episode;

import java.util.List;


public class InsertCommentsRunnable implements Runnable {
    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Comment> comments;

    public InsertCommentsRunnable(Context context, List<Comment> comments, DatabaseCallback<Void> callback) {
        this.context = context;
        this.comments = comments;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            RoomDatabaseFactory.db(context).showsAppDao().insertComments(comments);
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
