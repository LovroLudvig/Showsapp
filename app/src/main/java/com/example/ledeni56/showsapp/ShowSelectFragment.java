package com.example.ledeni56.showsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ShowSelectFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Show> shows;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_select,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.ShowRecyclerView);

        shows= ApplicationShows.getShows();
        fragmentManager=getFragmentManager();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShowsAdapter(shows, new OnShowFragmentInteractionListener() {
            @Override
            public void onShowSelected(int showId) {
                if (fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack();
                }
                EpisodeSelectFragment episodeSelectFragment = EpisodeSelectFragment.newInstance(showId);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit).replace(R.id.frameLayoutRight, episodeSelectFragment).addToBackStack("episode").commit();
            }
        }));
    }


    public interface OnShowFragmentInteractionListener {
        public void onShowSelected(int showId);
    }
}
