package com.example.ledeni56.showsapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;


public class AddEpisodeActivity extends AppCompatActivity {


    public static final String EPISODE="EpisodeSending";
    public static final String SAVE_URI="urisaver";
    public static final String SAVE_TITLE="titlesaver";
    public static final String SAVE_EPISODE_NUMBER="numbersaver";
    public static final String SAVE_SEASON_NUMBER="seasonsaver";
    public static final String SAVE_DESCRIPTION="descriptionsaver";
    public static final String SAVE_SEASONANDEPISODE_TEXT="seasonandepisodesaver";

    private final int REQUEST_CODE_PERMISSION=3;
    private final int REQUEST_CODE_PICTURE=2;



    private int showId;

    private EditText episodeNameView;
    private EditText episodeDescriptionView;
    private TextView selectedEpisodeText;
    private Button saveButton;
    private Button addPhotoButton;
    private ImageView episodePhoto;
    private TextView addPhotoText;

    private int episodeNumber;
    private int seasonNumber;
    private Uri episodeUriPicture=null;



    private Episode addedEpisode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);

        showId=getIntent().getIntExtra(EpisodeSelectActivity.SHOW_ID,-1);

        setMyActionBar();

        episodeNameView=findViewById(R.id.episodeName);
        episodeDescriptionView=findViewById(R.id.episodeDescription);
        saveButton=findViewById(R.id.saveButton);
        selectedEpisodeText=findViewById(R.id.selectedEpisode);
        addPhotoButton=findViewById(R.id.addPhotoButton);
        episodePhoto=findViewById(R.id.episodeImage);
        addPhotoText=findViewById(R.id.addphotoText);

        selectedEpisodeText.setText("Unknown");

        if (savedInstanceState!=null){
            episodeUriPicture=savedInstanceState.getParcelable(SAVE_URI);
            episodeNameView.setText(savedInstanceState.getString(SAVE_TITLE));
            episodeDescriptionView.setText(savedInstanceState.getString(SAVE_DESCRIPTION));
            selectedEpisodeText.setText(savedInstanceState.getString(SAVE_SEASONANDEPISODE_TEXT));

            if (episodeUriPicture!=null){
                addPhotoButton.setVisibility(View.GONE);
                addPhotoText.setVisibility(View.GONE);
                Glide.with(this).load(episodeUriPicture).into(episodePhoto);
            }
            if(!selectedEpisodeText.getText().toString().equals("Unknown")){
                episodeNumber=savedInstanceState.getInt(SAVE_EPISODE_NUMBER);
                seasonNumber=savedInstanceState.getInt(SAVE_SEASON_NUMBER);
            }
        }


        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddEpisodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddEpisodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                } else {
                    loadFromGallery();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()){
                    if (addEpisode()){
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        returnIntent.putExtra(EPISODE, addedEpisode);
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadFromGallery();
        } else {
            Toast.makeText(this,"Can't load image without permission",Toast.LENGTH_SHORT);
        }
    }

    //making implicit intent for Gallery
    private void loadFromGallery() {
        Intent i=new Intent(Intent.ACTION_PICK);

        String picturesPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        Uri data= Uri.parse(picturesPath);

        i.setDataAndType(data, "image/*");
        startActivityForResult(i, REQUEST_CODE_PICTURE);
    }


    //returning from picking picture in Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_PICTURE){
            if (resultCode==RESULT_OK){
                Uri result=data.getData();
                Glide.with(this).load(result).into(episodePhoto);
                addPhotoButton.setVisibility(View.GONE);
                addPhotoText.setVisibility(View.GONE);

                episodeUriPicture=result;
            }
        }
    }

    private boolean addEpisode() {
        addedEpisode=new Episode(
                episodeNameView.getText().toString(),
                episodeDescriptionView.getText().toString(),
                episodeNumber,
                seasonNumber,
                episodeUriPicture
        );

        if (ApplicationShows.addEpisodeToShow(addedEpisode, showId)){
            return true;
        }else{
            Toast.makeText(this, "Episode already exists, please check Season and Episode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkFields() {
        if (episodeNameView.getText().toString().equals("")){
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedEpisodeText.getText().toString().equals("Unknown")){
            Toast.makeText(this, "Select Episode and Season is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (episodeDescriptionView.getText().toString().equals("")){
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if(ApplicationShows.nameExists(episodeNameView.getText().toString(), showId)){
            Toast.makeText(this, "Name already exists, please check Title", Toast.LENGTH_SHORT).show();
            return false;
        } else{
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


    //Dialog for picking Season and Episode
    public void pickerOnClick(View view) {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.episode_picker);

        Button saveButton=dialog.findViewById(R.id.dialogSave);
        final NumberPicker seasonPicker=dialog.findViewById(R.id.dialogSeason);
        final NumberPicker episodePicker=dialog.findViewById(R.id.dialogEpisode);

        seasonPicker.setMinValue(1);
        seasonPicker.setMaxValue(20);

        episodePicker.setMaxValue(99);
        episodePicker.setMinValue(1);

        episodePicker.setWrapSelectorWheel(false);
        seasonPicker.setWrapSelectorWheel(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                episodeNumber=episodePicker.getValue();
                seasonNumber=seasonPicker.getValue();
                selectedEpisodeText.setText("Season "+String.valueOf(seasonNumber)+", Ep "+String.valueOf(episodeNumber));
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(SAVE_URI, episodeUriPicture);
        outState.putString(SAVE_TITLE, episodeNameView.getText().toString());
        outState.putString(SAVE_DESCRIPTION, episodeDescriptionView.getText().toString());
        outState.putInt(SAVE_EPISODE_NUMBER,episodeNumber);
        outState.putInt(SAVE_SEASON_NUMBER,seasonNumber);
        outState.putString(SAVE_SEASONANDEPISODE_TEXT,selectedEpisodeText.getText().toString());
    }


}
