package com.example.ledeni56.showsapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.Fragments.EpisodeSelectFragment;
import com.example.ledeni56.showsapp.R;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>  {
    private final List<Episode> episodes;
    private String showName;
    private Show show;

    private EpisodeSelectFragment.OnEpisodeFragmentInteractionListener listener;

    public EpisodesAdapter(Show show, EpisodeSelectFragment.OnEpisodeFragmentInteractionListener listener) {
        this.show=show;
        this.episodes=show.getEpisodes();
        this.showName=show.getName();
        this.listener=listener;
    }

    @NonNull
    @Override
    public EpisodesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episodes, parent, false);
        return new EpisodesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        TextView titleText = holder.itemView.findViewById(R.id.episodeText);
        TextView episodeAndSeason= holder.itemView.findViewById(R.id.seasonAndEpisodeText);



        final Episode episode = episodes.get(position);

        titleText.setText(episode.getName());
        String episodeAndSeasonText=fix("S"+String.valueOf(episode.getSeasonNumber())+" Ep"+String.valueOf(episode.getEpisodeNumber()));
        episodeAndSeason.setText(episodeAndSeasonText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEpisodeSelected(episode);
            }
        });
    }

    private String fix(String s) {
        while(s.length()<8){
            s=s+" ";
        }
        return s;
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
