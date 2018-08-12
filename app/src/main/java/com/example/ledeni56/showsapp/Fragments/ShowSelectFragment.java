package com.example.ledeni56.showsapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ledeni56.showsapp.Activities.LoginActivity;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Adapters.ShowsAdapter;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;
import com.example.ledeni56.showsapp.Networking.ApiEpisode;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.ApiShowDescription;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;
import com.example.ledeni56.showsapp.Static.ApplicationShows;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowSelectFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Show> shows;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private ToolbarProvider listener;
    private ProgressDialog progressDialog;
    private ShowsAppRepository showsAppRepository;

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
        if (showsAppRepository==null){
            showsAppRepository= ShowsAppRepository.get(getActivity());
        }
        setToolbar();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        recyclerView.setAdapter(new ShowsAdapter(shows, new OnShowFragmentInteractionListener() {
            @Override
            public void onShowSelected(final String showId) {
                if (fragmentManager.getBackStackEntryCount() > 1) {
                    fragmentManager.popBackStack();
                }
                if(isInternetAvailable()){
                    loadFromApi(showId);
                }else{
                    getFromDB(showId);
                }


            }
        }));
    }

    private void getFromDB(final String showId) {
        showsAppRepository.getShows(new DatabaseCallback<List<Show>>() {
            @Override
            public void onSuccess(List<Show> data) {
                for (Show databaseShow:data){
                    ApplicationShows.addShow(databaseShow);
                }
                showsAppRepository.getEpisodes(new DatabaseCallback<List<Episode>>() {
                    @Override
                    public void onSuccess(List<Episode> data) {
                        for (Episode ep:data){
                            ApplicationShows.addEpisodeToShow(ep,ep.getOwnerId());
                        }
                        EpisodeSelectFragment episodeSelectFragment = EpisodeSelectFragment.newInstance(showId);
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit).replace(R.id.frameLayoutRight, episodeSelectFragment).addToBackStack("episode").commit();
                    }

                    @Override
                    public void onError(Throwable t) {
                        showError("Unknown Error");
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                showError("Unknown Error");
            }
        });
    }

    private void insertIntoDb(final String showId){
        showsAppRepository.insertShows(ApplicationShows.getShows(), new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                showsAppRepository.insertEpisodes(ApplicationShows.getShow(showId).getEpisodes(), new DatabaseCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        EpisodeSelectFragment episodeSelectFragment = EpisodeSelectFragment.newInstance(showId);
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit).replace(R.id.frameLayoutRight, episodeSelectFragment).addToBackStack("episode").commit();
                    }

                    @Override
                    public void onError(Throwable t) {
                        showError("Unknown Error");
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                showError("Unknown Error");
            }
        });

    }
    private void loadFromApi(final String showId){
        showProgress();
        ApiServiceFactory.get().getShows(showId).enqueue(new Callback<ResponseData<ApiShowDescription>>() {
            @Override
            public void onResponse(Call<ResponseData<ApiShowDescription>> call, Response<ResponseData<ApiShowDescription>> response) {
                if (response.isSuccessful()){
                    ApplicationShows.getShow(showId).setDescription(response.body().getData().getDescription());
                    ApiServiceFactory.get().getEpisodes(showId).enqueue(new Callback<ResponseData<List<ApiEpisode>>>() {
                        @Override
                        public void onResponse(Call<ResponseData<List<ApiEpisode>>> call, Response<ResponseData<List<ApiEpisode>>> response) {
                            if(response.isSuccessful()){
                                hideProgress();
                                List<ApiEpisode> listEpisodes = response.body().getData();
                                for (ApiEpisode episode : listEpisodes) {
                                    if (episodeCorrect(episode)) {
                                        ApplicationShows.addEpisodeToShow(new Episode(episode.getId(),showId, episode.getName(), episode.getDescription(), episode.getEpisodeNumber(), episode.getSeasonNumber(), episode.getPicture()), showId);
                                    }
                                }
                                insertIntoDb(showId);
                            }else{
                                hideProgress();
                                showError("Unknown error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<List<ApiEpisode>>> call, Throwable t) {
                            hideProgress();
                            showError("Unknown error");

                        }
                    });
                }else{
                    hideProgress();
                    showError("Unknown error");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<ApiShowDescription>> call, Throwable t) {
                hideProgress();
                showError("Unknown error");

            }
        });
    }

    private boolean episodeCorrect(ApiEpisode episode) {
        if (!episode.getDescription().equals("") && !episode.getName().equals("") && !episode.getId().equals("") && !episode.getPicture().equals("")){
            try{
                Integer.valueOf(episode.getEpisodeNumber());
                Integer.valueOf(episode.getSeasonNumber());
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }

    protected void showError(String text) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error!")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    private void setToolbar() {
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.VISIBLE);
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

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setVisibility(View.GONE);
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
    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Loading...", true, false);
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public interface OnShowFragmentInteractionListener {
        public void onShowSelected(String showId);
    }
}
