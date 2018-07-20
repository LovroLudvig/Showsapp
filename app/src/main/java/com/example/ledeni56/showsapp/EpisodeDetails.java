package com.example.ledeni56.showsapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class EpisodeDetails extends AppCompatActivity {

    public static final String EPISODE="Episode";
    public static String SHOW_NAME="ShowName";

    private ImageView episodePicture;
    private TextView episodeTitle;
    private TextView episodeDescription;
    private TextView episodeNumber;

    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);

        episodePicture=findViewById(R.id.episodePicture);
        episodeTitle=findViewById(R.id.episodeTitle);
        episodeDescription=findViewById(R.id.episodeDescription);
        episodeNumber=findViewById(R.id.episodeNumber);

        episode=(Episode) getIntent().getParcelableExtra(EPISODE);

        setMyActionBar();

        Glide.with(this).load(episode.getPicture()).into(episodePicture);
        episodeTitle.setText(episode.getName());
        episodeDescription.setText(episode.getDescription());
        episodeNumber.setText("Season "+episode.getSeasonNumber()+", Ep "+episode.getEpisodeNumber());
    }

    private void setMyActionBar() {
        Toolbar myToolbar=findViewById(R.id.episodeDetailsToolbar);
        myToolbar.setTitle(getIntent().getStringExtra(SHOW_NAME));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
