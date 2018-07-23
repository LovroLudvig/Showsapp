package com.example.ledeni56.showsapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.view.View.GONE;


public class AddEpisodeFragment extends Fragment {
    private static final String ARG_NUMBER = "ARG_NUMBER";
    private static final int REQUEST_CODE_PERMISSION=1;
    private static final int REQUEST_CODE_PICTURE = 2;

    private int showId;
    private Show show;
    private Uri episodeUriPicture=null;

    private EditText episodeNameView;
    private EditText episodeDescriptionView;
    private TextView selectedEpisodeText;
    private Button saveButton;
    private RelativeLayout addPhotoLayout;
    private ImageView episodePhoto;
    private ImageView addPhotoIcon;
    private TextView addPhotoText;
    private View episodePicker;

    private Episode addedEpisode;
    private int episodeNumber;
    private int seasonNumber;

    private OnEpisodeAddFragmentInteractionListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_episode,container,false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show=ApplicationShows.getShow(showId);
        //AddEpisodeFields.resetFields();

        episodeNameView=view.findViewById(R.id.episodeName);
        episodeDescriptionView=view.findViewById(R.id.episodeDescription);
        saveButton=view.findViewById(R.id.saveButton);
        selectedEpisodeText=view.findViewById(R.id.selectedEpisode);
        addPhotoLayout=view.findViewById(R.id.addPhotoLayout);
        episodePhoto=view.findViewById(R.id.episodeImage);
        addPhotoText=view.findViewById(R.id.addPhotoText);
        addPhotoIcon=view.findViewById(R.id.addPhotoIcon);
        episodePicker=view.findViewById(R.id.episodePicker);

        selectedEpisodeText.setText("Unknown");

        addPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
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
                        listener.onEpisodeAdded(showId);
                    }
                }
            }
        });

        episodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getContext());
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
        });

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
            Toast.makeText(getContext(), "Episode already exists, please check Season and Episode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkFields() {
        if (episodeNameView.getText().toString().equals("")){
            Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedEpisodeText.getText().toString().equals("Unknown")){
            Toast.makeText(getContext(), "Select Episode and Season is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (episodeDescriptionView.getText().toString().equals("")){
            Toast.makeText(getContext(), "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if(ApplicationShows.nameExists(episodeNameView.getText().toString(), showId)){
            Toast.makeText(getContext(), "Name already exists, please check Title", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadFromGallery();
        } else {
            Toast.makeText(getContext(),"Can't load image without permission",Toast.LENGTH_SHORT);
        }
    }

    private void loadFromGallery() {
        Intent i=new Intent(Intent.ACTION_PICK);

        String picturesPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        Uri data= Uri.parse(picturesPath);

        i.setDataAndType(data, "image/*");
        startActivityForResult(i, REQUEST_CODE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_PICTURE){
            if (resultCode== Activity.RESULT_OK){
                Uri result=data.getData();
                Glide.with(this).load(result).into(episodePhoto);
                addPhotoIcon.setVisibility(GONE);
                addPhotoText.setVisibility(GONE);
                episodeUriPicture=result;
            }
        }
    }

    public static AddEpisodeFragment newInstance(int showId) {
        AddEpisodeFragment fragment = new AddEpisodeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, showId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showId = getArguments().getInt(ARG_NUMBER);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEpisodeAddFragmentInteractionListener) {
            listener = (OnEpisodeAddFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowFragmentInteractionListener");
        }
    }

    public Bundle getCurrentFields(){
        Bundle bun=new Bundle();
        bun.putString("episode name",episodeNameView.getText().toString());
        bun.putString("episode desc",episodeDescriptionView.getText().toString());
        bun.putString("episode and season text",selectedEpisodeText.getText().toString());
        bun.putParcelable("uri", episodeUriPicture);
        return bun;
    }

    public interface OnEpisodeAddFragmentInteractionListener {
        public void onEpisodeAdded(int showId);
    }


}
