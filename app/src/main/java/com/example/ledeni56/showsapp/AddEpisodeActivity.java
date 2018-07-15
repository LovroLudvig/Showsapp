package com.example.ledeni56.showsapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEpisodeActivity extends AppCompatActivity {

    private int idOfShow;

    private EditText episodeNameView;
    private EditText episodeDescriptionView;
    private Button saveButton;


    private Episode addedEpisode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);

        idOfShow=getIntent().getIntExtra("Id",-1);

        setMyActionBar();

        episodeNameView=findViewById(R.id.episodeName);
        episodeDescriptionView=findViewById(R.id.episodeDescription);
        saveButton=findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    if (addEpisode()){
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        returnIntent.putExtra("Name", addedEpisode.getName());
                        returnIntent.putExtra("Desc", addedEpisode.getDescription());
                        finish();
                    }
                }
            }
        });

    }

    private boolean addEpisode() {
        addedEpisode=new Episode(
                episodeNameView.getText().toString(),
                episodeDescriptionView.getText().toString()
        );

        if (ApplicationShows.addEpisodeToShow(addedEpisode, idOfShow)){
            return true;
        }else{
            Toast.makeText(this, "Episode already exists", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkFields() {
        if (episodeNameView.getText().toString().equals("")){
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (episodeDescriptionView.getText().toString().equals("")){
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void setMyActionBar() {
        Toolbar myToolbar = findViewById(R.id.addEpisodeToolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }
}
