package com.example.ledeni56.showsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noShowsText;

    private ArrayList<Show> shows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_select);

        recyclerView = findViewById(R.id.ShowRecyclerView);
        emptyImage=findViewById(R.id.noShowImage);
        noShowsText=findViewById(R.id.noShowsText);

        shows= ApplicationShows.getShows();

        setMyActionBar();
        checkIfShowsEmpty();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ShowsAdapter(shows, getApplicationContext()));
    }

    private void setMyActionBar() {
        Toolbar myToolbar = findViewById(R.id.showToolbar);
        setSupportActionBar(myToolbar);
    }

    private void checkIfShowsEmpty() {
        if (shows.size()==0){
            recyclerView.setVisibility(View.GONE);
            emptyImage.setVisibility(View.VISIBLE);
            noShowsText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.GONE);
            noShowsText.setVisibility(View.GONE);
        }

    }

}
