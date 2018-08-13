package com.example.ledeni56.showsapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Fragments.EpisodeSelectFragment;
import com.example.ledeni56.showsapp.R;

import java.util.ArrayList;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {
    private ArrayList<Episode> episodes;
    private String showName;

    private EpisodeSelectFragment.OnEpisodeFragmentInteractionListener listener;

    public EpisodesAdapter(ArrayList<Episode> episodes, int from, int to, String showName, EpisodeSelectFragment.OnEpisodeFragmentInteractionListener listener) {
        if (from == 0 && to == 0) {
            this.episodes = episodes;
        } else {
            this.episodes = new ArrayList<>();
            for (int i = from; i < to; i++) {
                if (i < episodes.size()) {
                    this.episodes.add(episodes.get(i));
                }
            }
        }
        this.showName = showName;
        this.listener = listener;
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
        TextView episodeAndSeason = holder.itemView.findViewById(R.id.seasonAndEpisodeText);


        final Episode episode = episodes.get(position);

        titleText.setText(episode.getName());
        String episodeAndSeasonText = fix("S" + String.valueOf(episode.getSeasonNumber()) + " Ep" + String.valueOf(episode.getEpisodeNumber()));
        episodeAndSeason.setText(episodeAndSeasonText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEpisodeSelected(episode);
            }
        });
    }

    private String fix(String s) {
        while (s.length() < 8) {
            s = s + " ";
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
