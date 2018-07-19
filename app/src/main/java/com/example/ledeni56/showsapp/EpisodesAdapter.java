package com.example.ledeni56.showsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder>  {
    private final List<Episode> episodes;
    private Context context;

    public EpisodesAdapter(List<Episode> episodes, Context context) {
        this.episodes = episodes;
        this.context=context;
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
        ImageView episodeImage=holder.itemView.findViewById(R.id.episodePicture);



        final Episode episode = episodes.get(position);

        titleText.setText(episode.getName());
        episodeAndSeason.setText("Season "+String.valueOf(episode.getSeasonNumber())+", Ep "+String.valueOf(episode.getEpisodeNumber()));
        Glide.with(holder.itemView.getContext()).load(episode.getPicture()).into(episodeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "Episode description: " +episode.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
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
