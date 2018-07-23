package com.example.ledeni56.showsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by lovro on 7/23/2018.
 */

public class EpisodeDetailsFragment extends Fragment{

    private static final String ARG_EPISODE = "arg episode";
    private Episode episode;

    private ImageView episodePicture;
    private TextView episodeTitle;
    private TextView episodeDescription;
    private TextView episodeNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        episodePicture=view.findViewById(R.id.episodePicture);
        episodeTitle=view.findViewById(R.id.episodeTitle);
        episodeDescription=view.findViewById(R.id.episodeDescription);
        episodeNumber=view.findViewById(R.id.episodeNumber);

        Glide.with(this).load(episode.getPicture()).into(episodePicture);
        episodeTitle.setText(episode.getName());
        episodeDescription.setText(episode.getDescription());
        episodeNumber.setText("Season "+episode.getSeasonNumber()+", Ep "+episode.getEpisodeNumber());


    }

    public static EpisodeDetailsFragment newInstance(Episode episode) {
        EpisodeDetailsFragment fragment = new EpisodeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EPISODE, episode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            episode = getArguments().getParcelable(ARG_EPISODE);
        }
    }
}
