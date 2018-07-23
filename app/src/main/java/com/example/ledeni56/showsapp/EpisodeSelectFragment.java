package com.example.ledeni56.showsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class EpisodeSelectFragment extends Fragment {

    private static final String ARG_NUMBER="Arg_number";
    private int showId;

    private OnEpisodeFragmentInteractionListener listener;

    private Show show;
    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_select,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        episodesShowing=ApplicationShows.showEpisodes(showId);
        recyclerView = view.findViewById(R.id.EpisodeRecyclerView);
        emptyImage=view.findViewById(R.id.noEpisodesImage);
        noEpisodeText=view.findViewById(R.id.noEpisodesText);
        show=ApplicationShows.getShow(showId);

        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EpisodesAdapter(show,listener));

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


    public static EpisodeSelectFragment newInstance(int showId) {
        EpisodeSelectFragment fragment = new EpisodeSelectFragment();
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
        if (context instanceof OnEpisodeFragmentInteractionListener) {
            listener = (OnEpisodeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowFragmentInteractionListener");
        }
    }
    public interface OnEpisodeFragmentInteractionListener {
        public void onEpisodeSelected(Episode episode);
    }
}
