package com.example.ledeni56.showsapp.Room;

import android.content.Context;

import com.example.ledeni56.showsapp.Entities.DatabaseShow;
import com.example.ledeni56.showsapp.Entities.Episode;
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

    public void getShows(DatabaseCallback<List<DatabaseShow>> callback) {
        cancel();
        this.task = executor.submit(new GetShowsRunnable(context, callback));
    }
    public void getEpisodes(DatabaseCallback<List<Episode>> callback) {
        cancel();
        this.task = executor.submit(new GetEpisodesRunnable(context, callback));
    }

    public void insertShows(List<DatabaseShow> shows, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertShowsRunnable(context, shows, callback));
    }

    public void insertEpisodes(List<Episode> episodes, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertEpisodesRunnable(context, episodes, callback));
    }

    private void cancel() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }
}
