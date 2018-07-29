package com.example.ledeni56.showsapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.ApiShowId;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;
import com.example.ledeni56.showsapp.Static.ApplicationShows;

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
        showsAppRepository.getShows(new DatabaseCallback<List<Show>>() {
            @Override
            public void onSuccess(List<Show> data) {
                for (Show databaseShow:data){
                    ApplicationShows.addShow(databaseShow);
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
        showsAppRepository.insertShows(ApplicationShows.getShows(), new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                continueCreate();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

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
