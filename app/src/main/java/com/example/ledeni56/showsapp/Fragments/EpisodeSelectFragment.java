package com.example.ledeni56.showsapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ledeni56.showsapp.Activities.LoginActivity;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Adapters.EpisodesAdapter;
import com.example.ledeni56.showsapp.Static.ApplicationShows;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class EpisodeSelectFragment extends Fragment {

    private static final String ARG_NUMBER="Arg_number";
    private String showId;

    private ToolbarProvider listener;

    private Show show;
    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageView;
    private Toolbar animToolbar;
    private TextView showDescription;
    private FloatingActionButton addBurron;
    private TextView episodeCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_select,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        episodesShowing= ApplicationShows.showEpisodes(showId);
        recyclerView = view.findViewById(R.id.EpisodeRecyclerView);
        emptyImage=view.findViewById(R.id.noEpisodesImage);
        noEpisodeText=view.findViewById(R.id.noEpisodesText);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbarLayout);
        imageView=view.findViewById(R.id.header);
        show=ApplicationShows.getShow(showId);
        animToolbar=view.findViewById(R.id.anim_toolbar);
        showDescription=view.findViewById(R.id.showDescription);
        addBurron=view.findViewById(R.id.addEpisode);
        episodeCount=view.findViewById(R.id.episodesCount);


        fragmentManager=getFragmentManager();

        setToolbar(view);
        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EpisodesAdapter(show, new OnEpisodeFragmentInteractionListener() {
            @Override
            public void onEpisodeSelected(Episode episode) {
                EpisodeDetailsFragment episodeDetailsFragment = EpisodeDetailsFragment.newInstance(episode,show);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(R.id.frameLayoutRight,episodeDetailsFragment).addToBackStack("details").commit();
            }
        }));

        addBurron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEpisodeFragment addEpisodeFragment=AddEpisodeFragment.newInstance(show.getId());
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_up,R.anim.exit_up,R.anim.enter_up,R.anim.exit_up).replace(R.id.frameLayoutRight,addEpisodeFragment).addToBackStack("add Episode").commit();
            }
        });
    }


    private void setToolbar(View view) {
        episodeCount.setText(String.valueOf(show.getEpisodes().size()));
        showDescription.setText("    "+show.getDescription());
        collapsingToolbar.setTitle(show.getName());
        Glide.with(getContext()).load(Uri.parse(show.getPictureUrl())).into(imageView);
        animToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_register_24dp);
        animToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(show.getName());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void checkIfEpisodesEmpty() {
        if (episodesShowing.size()==0){
            emptyImage.setVisibility(View.VISIBLE);
            noEpisodeText.setVisibility(View.VISIBLE);
        } else{
            emptyImage.setVisibility(View.GONE);
            noEpisodeText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.GONE);
    }

    public static EpisodeSelectFragment newInstance(String showId) {
        EpisodeSelectFragment fragment = new EpisodeSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, showId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showId = getArguments().getString(ARG_NUMBER);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarProvider) {
            listener = (ToolbarProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToolbarProvider");
        }
    }


    public interface OnEpisodeFragmentInteractionListener {
        public void onEpisodeSelected(Episode episode);
    }
}
