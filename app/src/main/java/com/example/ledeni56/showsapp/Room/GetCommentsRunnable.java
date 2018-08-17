package com.example.ledeni56.showsapp.Room;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.Episode;

import java.util.List;


public class GetCommentsRunnable implements Runnable {
    private final DatabaseCallback<List<Comment>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetCommentsRunnable(Context context, DatabaseCallback<List<Comment>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<Comment> comments = RoomDatabaseFactory.db(context).showsAppDao().getComments();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(comments);
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
