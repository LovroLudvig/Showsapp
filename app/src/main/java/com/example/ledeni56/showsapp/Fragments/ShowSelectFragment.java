package com.example.ledeni56.showsapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ledeni56.showsapp.Activities.LoginActivity;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Adapters.ShowsAdapter;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;
import com.example.ledeni56.showsapp.Static.ApplicationShows;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;

import java.util.ArrayList;


public class ShowSelectFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Show> shows;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private ToolbarProvider listener;

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
        setToolbar();

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
    private void setToolbar() {
        toolbar=listener.getToolbar();
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_show);
        toolbar.setTitle("Shows");
        toolbar.setNavigationIcon(null);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_logout:
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, getActivity().MODE_PRIVATE).edit();
                        editor.putString(MainActivity.TOKEN_KEY, null);
                        editor.commit();
                        Intent i=new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    default:
                        return false;
                }
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
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

    public interface OnShowFragmentInteractionListener {
        public void onShowSelected(int showId);
    }
}
