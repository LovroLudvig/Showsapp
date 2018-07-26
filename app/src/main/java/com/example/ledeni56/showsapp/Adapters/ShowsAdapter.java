package com.example.ledeni56.showsapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.Fragments.ShowSelectFragment;
import com.example.ledeni56.showsapp.R;

import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {


    private final List<Show> shows;
    private ShowSelectFragment.OnShowFragmentInteractionListener listener;

    public ShowsAdapter(List<Show> shows, ShowSelectFragment.OnShowFragmentInteractionListener listener) {
            this.shows = shows;
            this.listener=listener;
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
        final Context context=holder.itemView.getContext();

      

        textView.setText(show.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShowSelected(show.getID());
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

