package com.example.ledeni56.showsapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class EpisodeSelectFragment extends Fragment {

    private static final String ARG_NUMBER="Arg_number";
    private int showId;

    private ToolbarProvider listener;

    private Show show;
    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;

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

        fragmentManager=getFragmentManager();

        setToolbar();

        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EpisodesAdapter(show, new OnEpisodeFragmentInteractionListener() {
            @Override
            public void onEpisodeSelected(Episode episode) {
                EpisodeDetailsFragment episodeDetailsFragment = EpisodeDetailsFragment.newInstance(episode,show);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(R.id.frameLayoutRight,episodeDetailsFragment).addToBackStack("details").commit();
            }
        }));

    }

    private void setToolbar() {
        toolbar=listener.getToolbar();
        toolbar.getMenu().clear();
        toolbar.setTitle(show.getName());
        toolbar.inflateMenu(R.menu.menu_episode);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        AddEpisodeFragment addEpisodeFragment=AddEpisodeFragment.newInstance(show.getID());
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_up,R.anim.exit_up,R.anim.enter_up,R.anim.exit_up).replace(R.id.frameLayoutRight,addEpisodeFragment).addToBackStack("add Episode").commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
        Menu menu=toolbar.getMenu();
        menu.clear();
        toolbar.setTitle("Shows");
        toolbar.setNavigationIcon(null);
    }

    public interface OnEpisodeFragmentInteractionListener {
        public void onEpisodeSelected(Episode episode);
    }
}
