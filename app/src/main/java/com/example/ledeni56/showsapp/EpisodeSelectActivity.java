package com.example.ledeni56.showsapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EpisodeSelectActivity extends AppCompatActivity {

    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_select);

        Intent i=getIntent();
        episodesShowing=i.getParcelableArrayListExtra("Episodes");
        setMyActionBar(i.getStringExtra("Name"));


        recyclerView = findViewById(R.id.EpisodeRecyclerView);
        emptyImage=findViewById(R.id.noEpisodesImage);
        noEpisodeText=findViewById(R.id.noEpisodesText);

        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EpisodesAdapter(episodesShowing, getApplicationContext()));

    }

    private void setMyActionBar(String showName) {
        Toolbar myToolbar = findViewById(R.id.episodeToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(showName);
    }

    private void checkIfEpisodesEmpty() {
        if (episodesShowing.size()==0){
            recyclerView.setVisibility(View.GONE);
            emptyImage.setVisibility(View.VISIBLE);
            noEpisodeText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyImage.setVisibility(View.GONE);
            noEpisodeText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_episode, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;

            case R.id.action_add:
                Intent i=new Intent(EpisodeSelectActivity.this, AddEpisodeActivity.class);
                i.putExtra("Id", getIntent().getIntExtra("Id",-1));
                startActivityForResult(i, 1);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Episode ep=new Episode(data.getStringExtra("Name"), data.getStringExtra("Desc"));
                episodesShowing.add(ep);
                recyclerView.getAdapter().notifyItemInserted(episodesShowing.size()-1);
                checkIfEpisodesEmpty();
            }
        }
    }
}
