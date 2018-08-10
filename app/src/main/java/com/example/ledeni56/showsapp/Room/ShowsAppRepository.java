package com.example.ledeni56.showsapp.Room;

import android.content.Context;

import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.DislikedShow;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.LikedShow;
import com.example.ledeni56.showsapp.Entities.Show;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ShowsAppRepository {
    private Context context;
    private ExecutorService executor;
    private Future task;

    public static ShowsAppRepository get(Context context){
        return new ShowsAppRepository(context);
    }

    private ShowsAppRepository(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void getShows(DatabaseCallback<List<Show>> callback) {
        cancel();
        this.task = executor.submit(new GetShowsRunnable(context, callback));
    }

    public void getComments(DatabaseCallback<List<Comment>> callback) {
        cancel();
        this.task = executor.submit(new GetCommentsRunnable(context, callback));
    }

    public void getEpisodes(DatabaseCallback<List<Episode>> callback) {
        cancel();
        this.task = executor.submit(new GetEpisodesRunnable(context, callback));
    }

    public void insertShows(List<Show> shows, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertShowsRunnable(context, shows, callback));
    }

    public void insertComments(List<Comment> comments, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertCommentsRunnable(context, comments, callback));
    }

    public void insertEpisodes(List<Episode> episodes, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertEpisodesRunnable(context, episodes, callback));
    }

    public void insertLikedShow(LikedShow likedShow, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertLikedShowRunnable(context, likedShow, callback));
    }

    public void getLikedShows(DatabaseCallback<List<LikedShow>> callback) {
        cancel();
        this.task = executor.submit(new GetLikedShowsRunnable(context, callback));
    }

    public void insertDislikedShow(DislikedShow dislikedShow, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertDislikedShowsRunnable(context, dislikedShow, callback));
    }

    public void deleteDislikedShow(DislikedShow dislikedShow, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new DeleteDislikedShowRunnable(context, dislikedShow, callback));
    }

    public void deleteLikedShow(LikedShow likedShow, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new DeleteLikedShowRunnable(context, likedShow, callback));
    }

    public void getDislikedShow(DatabaseCallback<List<DislikedShow>> callback) {
        cancel();
        this.task = executor.submit(new GetDislikedShowsRunnable(context, callback));
    }

    private void cancel() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }
}
