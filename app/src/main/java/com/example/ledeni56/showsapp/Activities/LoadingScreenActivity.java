package com.example.ledeni56.showsapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ledeni56.showsapp.Entities.DatabaseShow;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.Networking.ApiEpisode;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.ApiShowDescription;
import com.example.ledeni56.showsapp.Networking.ApiShowId;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;
import com.example.ledeni56.showsapp.Static.ApplicationShows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingScreenActivity extends BasicActivity {
    ImageView imageView;
    TextView textView;
    private ShowsAppRepository showsAppRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_screen);

        imageView=findViewById(R.id.logoImage);
        textView=findViewById(R.id.showText);

        if (showsAppRepository==null){
            showsAppRepository=ShowsAppRepository.get(this);
        }

        final ObjectAnimator textAnimator = ObjectAnimator.ofFloat(textView, "textSize", 50);
        textAnimator.setDuration(2000);
        textAnimator.setInterpolator(new OvershootInterpolator());
        textAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isInternetAvailable()){
                    loadShowsFromApi();
                }else{
                    getShowsDb();
                }


            }
        });


        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY",
                0f, Resources.getSystem().getDisplayMetrics().heightPixels/2);
        animator.setDuration(2000);
        animator.setInterpolator(new BounceInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                textView.setVisibility(View.VISIBLE);
                textAnimator.start();
            }
        });
        animator.start();

    }

    private void getShowsDb() {
        showsAppRepository.getShows(new DatabaseCallback<List<DatabaseShow>>() {
            @Override
            public void onSuccess(List<DatabaseShow> data) {
                for (DatabaseShow databaseShow:data){
                    ApplicationShows.addShow(new Show(databaseShow.getId(), databaseShow.getName(), databaseShow.getDescription(),databaseShow.getPictureUrl()));
                }
                if (ApplicationShows.getShows().size()==0){
                    loadingScreenError();
                }else{
                    continueCreate();
                }
            }

            @Override
            public void onError(Throwable t) {
                showError("Unknown error. Please exit application and try again.");
            }
        });
    }

    private void insertShowsDb() {
        ArrayList<DatabaseShow> databaseShows=new ArrayList<>();
        for (Show show:ApplicationShows.getShows()){
            databaseShows.add(new DatabaseShow(show.getID(),show.getName(),show.getDescription(),show.getPictureUrl()));
        }
        showsAppRepository.insertShows(databaseShows, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                continueCreate();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }


//    private void loadShowsFromApi() {
//        ApiServiceFactory.get().getShowIds().enqueue(new Callback<ResponseData<List<ApiShowId>>>() {
//            @Override
//            public void onResponse(Call<ResponseData<List<ApiShowId>>> call, Response<ResponseData<List<ApiShowId>>> response) {
//                if (response.isSuccessful()){
//                    List<ApiShowId> showIdList=response.body().getData();
//                    for (ApiShowId showId:showIdList){
//                        ApiServiceFactory.get().getShows(showId.getId()).enqueue(new Callback<ResponseData<ApiShowDescription>>() {
//                            @Override
//                            public void onResponse(Call<ResponseData<ApiShowDescription>> call, Response<ResponseData<ApiShowDescription>> response) {
//                                if(response.isSuccessful()){
//                                    final ApiShowDescription showDescription=response.body().getData();
//                                    ApplicationShows.addShow(new Show(showDescription.getId(),showDescription.getName(),showDescription.getDescription(),showDescription.getPictureUrl()));
//                                    ApiServiceFactory.get().getEpisodes(showDescription.getId()).enqueue(new Callback<ResponseData<List<ApiEpisode>>>() {
//                                        @Override
//                                        public void onResponse(Call<ResponseData<List<ApiEpisode>>> call, Response<ResponseData<List<ApiEpisode>>> response) {
//                                            if (response.isSuccessful()) {
//                                                List<ApiEpisode> listEpisodes = response.body().getData();
//                                                for (ApiEpisode episode : listEpisodes) {
//                                                    if (episodeCorrect(episode)) {
//                                                        ApplicationShows.addEpisodeToShow(new Episode(episode.getId(), episode.getName(), episode.getDescription(), episode.getEpisodeNumber(), episode.getSeasonNumber(), episode.getPicture()), showDescription.getId());
//                                                    }
//                                                }
//
//                                            }else{
//                                                showError("Unknown error");
//                                                continueCreate();
//                                            }
//                                        }
//                                        @Override
//                                        public void onFailure(Call<ResponseData<List<ApiEpisode>>> call, Throwable t) {
//                                            showError("Unknown error");
//                                            continueCreate();
//                                        }
//                                    });
//                                }else{
//                                    showError("Unknown error");
//                                    continueCreate();
//                                }
//                            }
//                            @Override
//                            public void onFailure(Call<ResponseData<ApiShowDescription>> call, Throwable t) {
//                                showError("Unknown error");
//                                continueCreate();
//                            }
//                        });
//                    }
//                    continueCreate();
//                }else{
//                    showError("Unknown error");
//                    continueCreate();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseData<List<ApiShowId>>> call, Throwable t) {
//                showError("Unknown error");
//                continueCreate();
//            }
//        });
//    }
//    private void loadShowsFromApi(){
//        Thread thread= new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    List<ApiShowId> response=ApiServiceFactory.get().getShowIds().execute().body().getData();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    private void loadShowsFromApi(){
        ApiServiceFactory.get().getShowIds().enqueue(new Callback<ResponseData<List<ApiShowId>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<ApiShowId>>> call, Response<ResponseData<List<ApiShowId>>> response) {
                if (response.isSuccessful()) {
                    List<ApiShowId> showIdList = response.body().getData();
                    for (ApiShowId apiShowId:showIdList){
                        ApplicationShows.addShow(new Show(apiShowId.getId(),apiShowId.getName(),"",apiShowId.getPictureUrl()));
                    }
                    insertShowsDb();
                }else{
                    showError("Unknown error. Please exit application and try again.");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<List<ApiShowId>>> call, Throwable t) {
                showError("Unknown error. Please exit application and try again.");
            }
        });
    }



    private void continueCreate() {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
