package com.example.ledeni56.showsapp;

import android.app.Activity;
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

import java.util.ArrayList;


public class ShowSelectFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Show> shows;
    private OnShowFragmentInteractionListener listener;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShowsAdapter(shows, listener));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShowFragmentInteractionListener) {
            listener = (OnShowFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShowFragmentInteractionListener");
        }
    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }

    public interface OnShowFragmentInteractionListener {
        public void onShowSelected(int showId);
    }
}
