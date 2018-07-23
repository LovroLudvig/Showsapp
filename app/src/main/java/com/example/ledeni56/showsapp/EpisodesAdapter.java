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
        ImageView episodeImage=holder.itemView.findViewById(R.id.episodePicture);



        final Episode episode = episodes.get(position);

        titleText.setText(episode.getName());
        episodeAndSeason.setText("Season "+String.valueOf(episode.getSeasonNumber())+", Ep "+String.valueOf(episode.getEpisodeNumber()));
        Glide.with(holder.itemView.getContext()).load(episode.getPicture()).into(episodeImage);

        if (episode.getPicture()==null){
            episodeImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEpisodeSelected(episode);

//                Intent i=new Intent(holder.itemView.getContext(), EpisodeDetails.class);
//                i.putExtra(EpisodeDetails.EPISODE,episode);
//                i.putExtra(EpisodeDetails.SHOW_NAME, showName);
//                holder.itemView.getContext().startActivity(i);
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
