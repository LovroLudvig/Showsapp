package com.example.ledeni56.showsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

    public static final String SHOW_NAME="ShowName";
    public static final String SHOW_ID="ShowId";
    public static final String EPISODES_LIST="EpisodesList";
    public static final int RESULT_CODE_EPISODE=1;

    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_select);

        Intent i=getIntent();
        episodesShowing=i.getParcelableArrayListExtra(EPISODES_LIST);
        setMyActionBar(i.getStringExtra(SHOW_NAME));


        recyclerView = findViewById(R.id.EpisodeRecyclerView);
        emptyImage=findViewById(R.id.noEpisodesImage);
        noEpisodeText=findViewById(R.id.noEpisodesText);

        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new EpisodesAdapter(episodesShowing, i.getStringExtra(SHOW_NAME)));

    }

    private void setMyActionBar(String showName) {
        Toolbar myToolbar = findViewById(R.id.episodeToolbar);
        myToolbar.inflateMenu(R.menu.menu_episode);
        myToolbar.setTitle(showName);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent i=new Intent(EpisodeSelectActivity.this, AddEpisodeActivity.class);
                        i.putExtra(SHOW_ID, getIntent().getIntExtra(SHOW_ID,-1));
                        startActivityForResult(i, RESULT_CODE_EPISODE);
                        return true;

                    default:
                        // If we got here, the user's action was not recognized.
                        // Invoke the superclass to handle it.
                        return false;
                }
            }
        });
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CODE_EPISODE) {
            if(resultCode == Activity.RESULT_OK){
                episodesShowing.add((Episode) data.getParcelableExtra(AddEpisodeActivity.EPISODE));
                recyclerView.getAdapter().notifyItemInserted(episodesShowing.size()-1);
                checkIfEpisodesEmpty();
            }
        }
    }
}
