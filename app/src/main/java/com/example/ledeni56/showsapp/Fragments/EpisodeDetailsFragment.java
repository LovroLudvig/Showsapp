package com.example.ledeni56.showsapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ledeni56.showsapp.Adapters.EpisodesAdapter;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;
import com.example.ledeni56.showsapp.Static.ApplicationShows;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class EpisodeDetailsFragment extends Fragment{

    private static final String ARG_EPISODE = "arg episode";
    private static final String ARG_SHOW_ID = "arg id";
    private Episode episode;

    private ImageView episodePicture;
    private TextView episodeTitle;
    private TextView episodeDescription;
    private TextView episodeNumber;
    private ToolbarProvider listener;
    private Toolbar toolbar;
    private String showName;
    private View commentsView;
    private Show show;
    private RecyclerView recycleView;
    private FragmentManager fragmentManager;

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
        commentsView=view.findViewById(R.id.commentsView);
        recycleView=view.findViewById(R.id.fiveEpisodes);

        fragmentManager=getFragmentManager();

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(new EpisodesAdapter(show.getEpisodes(),show.getEpisodes().indexOf(episode)+1,show.getEpisodes().indexOf(episode)+6,show.getName(), new EpisodeSelectFragment.OnEpisodeFragmentInteractionListener() {
            @Override
            public void onEpisodeSelected(Episode episode) {
                EpisodeDetailsFragment episodeDetailsFragment = EpisodeDetailsFragment.newInstance(episode,show);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(R.id.frameLayoutRight,episodeDetailsFragment).addToBackStack("details").commit();
            }
        }));

        commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpisodeCommentsFragment episodeCommentsFragment = EpisodeCommentsFragment.newInstance(episode);
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(R.id.frameLayoutRight,episodeCommentsFragment).addToBackStack("comments").commit();

            }
        });

        //OverScrollDecoratorHelper.setUpOverScroll((ScrollView) view.findViewById(R.id.scrollView));
        setMyToolbar();

        Glide.with(this).load(episode.getPicture()).into(episodePicture);
        episodeTitle.setText(episode.getName());
        episodeDescription.setText(episode.getDescription());
        episodeNumber.setText("Season "+episode.getSeasonNumber()+", Ep "+episode.getEpisodeNumber());
    }

    private void setMyToolbar() {
        toolbar=listener.getToolbar();
        toolbar.getMenu().clear();
        toolbar.setTitle(showName);
//        toolbar.inflateMenu(R.menu.menu_episode);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
//        toolbar.getMenu().findItem(R.id.action_add).setVisible(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.VISIBLE);
    }

    public static EpisodeDetailsFragment newInstance(Episode episode, Show show) {
        EpisodeDetailsFragment fragment = new EpisodeDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EPISODE, episode);
        args.putString(ARG_SHOW_ID, show.getId());
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onDetach() {
        super.onDetach();
//        toolbar.setVisibility(View.GONE);
//        toolbar.getMenu().findItem(R.id.action_add).setVisible(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            episode = getArguments().getParcelable(ARG_EPISODE);
            show= ApplicationShows.getShow(getArguments().getString(ARG_SHOW_ID));
            showName=show.getName();
        }
    }
}
