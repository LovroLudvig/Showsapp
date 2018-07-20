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

import java.util.ArrayList;
import java.util.List;

class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

        private final List<Show> shows;

    public ShowsAdapter(List<Show> shows) {
            this.shows = shows;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shows, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            TextView textView = holder.itemView.findViewById(R.id.showText);

            final Show show = shows.get(position);

            textView.setText(show.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(holder.itemView.getContext(), EpisodeSelectActivity.class);

                    ArrayList<Episode> episodes = show.getEpisodes();

                    i.putParcelableArrayListExtra(EpisodeSelectActivity.EPISODES_LIST, episodes);
                    i.putExtra(EpisodeSelectActivity.SHOW_NAME, show.getName());
                    i.putExtra(EpisodeSelectActivity.SHOW_ID, show.getID());

                    holder.itemView.getContext().startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return shows.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
}

