package com.example.ledeni56.showsapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Adapters.EpisodesAdapter;
import com.example.ledeni56.showsapp.Entities.DislikedShow;
import com.example.ledeni56.showsapp.Entities.LikedShow;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.ResponseLikeDislike;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;
import com.example.ledeni56.showsapp.Static.ApplicationShows;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EpisodeSelectFragment extends Fragment {

    private static final String ARG_NUMBER="Arg_number";
    private String showId;

    private ToolbarProvider listener;

    private Show show;
    private ArrayList<Episode> episodesShowing;
    private RecyclerView recyclerView;
    private ImageView emptyImage;
    private TextView noEpisodeText;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageView;
    private Toolbar animToolbar;
    private TextView showDescription;
    private FloatingActionButton addBurron;
    private TextView episodeCount;
    private ProgressDialog progressDialog;
    private ShowsAppRepository showsAppRepository;
    private TextView likesCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_select,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        episodesShowing= ApplicationShows.showEpisodes(showId);
        recyclerView = view.findViewById(R.id.EpisodeRecyclerView);
        emptyImage=view.findViewById(R.id.noEpisodesImage);
        noEpisodeText=view.findViewById(R.id.noEpisodesText);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbarLayout);
        imageView=view.findViewById(R.id.header);
        show=ApplicationShows.getShow(showId);
        animToolbar=view.findViewById(R.id.anim_toolbar);
        showDescription=view.findViewById(R.id.showDescription);
        addBurron=view.findViewById(R.id.addEpisode);
        episodeCount=view.findViewById(R.id.episodesCount);
        likesCount=view.findViewById(R.id.likeCount);

        if (showsAppRepository==null){
            showsAppRepository= ShowsAppRepository.get(getActivity());
        }


        fragmentManager=getFragmentManager();

        setToolbar(view);
        checkIfEpisodesEmpty();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new EpisodesAdapter(show.getEpisodes(),0,0,show.getName(), new OnEpisodeFragmentInteractionListener() {
            @Override
            public void onEpisodeSelected(Episode episode) {
                EpisodeDetailsFragment episodeDetailsFragment = EpisodeDetailsFragment.newInstance(episode,show);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_down,R.anim.exit_down,R.anim.enter_down,R.anim.exit_down).replace(R.id.frameLayoutRight,episodeDetailsFragment).addToBackStack("details").commit();
            }
        }));

        addBurron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEpisodeFragment addEpisodeFragment=AddEpisodeFragment.newInstance(show.getId());
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_up,R.anim.exit_up,R.anim.enter_up,R.anim.exit_up).replace(R.id.frameLayoutRight,addEpisodeFragment).addToBackStack("add Episode").commit();
            }
        });
        initLikes(view);
    }

    private void initLikes(View view) {
        ImageButton likeButton= view.findViewById(R.id.likeButton);
        ImageButton dislikeButton= view.findViewById(R.id.dislikeButton);

        likesCount.setText(String.valueOf(show.getLikesCount()));

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()){
                    likeShow();
                }else{
                    Toast.makeText(getActivity(),"Internet is not available.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()){
                    dislikeShow();
                }else{
                    Toast.makeText(getActivity(),"Internet is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dislikeShow() {
        showsAppRepository.getDislikedShow(new DatabaseCallback<List<DislikedShow>>() {
            @Override
            public void onSuccess(List<DislikedShow> data) {
                for (DislikedShow dislikedShow:data) {
                    if (dislikedShow.getId().equals(showId)) {
                        return;
                    }
                }
                showsAppRepository.getLikedShows(new DatabaseCallback<List<LikedShow>>() {
                    @Override
                    public void onSuccess(List<LikedShow> data) {
                        if(dataContainsLikedShow(showId, data)){
                            showProgress();
                            ApiServiceFactory.get().dislikeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                @Override
                                public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                    if (response.isSuccessful()){
                                        ApiServiceFactory.get().dislikeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                            @Override
                                            public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                                if (response.isSuccessful()){
                                                    hideProgress();
                                                    likesCount.setText(String.valueOf(response.body().getLikesCount()));
                                                }else{
                                                    hideProgress();
                                                    showError("error");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                                hideProgress();
                                                showError("error");
                                            }
                                        });
                                    }else{
                                        hideProgress();
                                        showError("error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                    hideProgress();
                                    showError("error");
                                }
                            });


                        }else{
                            ApiServiceFactory.get().dislikeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                @Override
                                public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                    if (response.isSuccessful()){
                                        hideProgress();
                                        likesCount.setText(String.valueOf(response.body().getLikesCount()));
                                    }else{
                                        hideProgress();
                                        showError("error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                    hideProgress();
                                    showError("error");
                                }
                            });
                        }
                        showsAppRepository.deleteLikedShow(new LikedShow(showId), new DatabaseCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                showsAppRepository.insertDislikedShow(new DislikedShow(showId), new DatabaseCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void data) {

                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        hideProgress();
                                        showError("error");
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable t) {
                                hideProgress();
                                showError("error");
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable t) {
                        hideProgress();
                        showError("error");
                    }
                });
            }
            @Override
            public void onError(Throwable t) {
                hideProgress();
                showError("error");
            }
        });
    }

    private void likeShow() {
        showsAppRepository.getLikedShows(new DatabaseCallback<List<LikedShow>>() {
            @Override
            public void onSuccess(List<LikedShow> data) {
                for (LikedShow likedShow:data) {
                    if (likedShow.getId().equals(showId)) {
                        return;
                    }
                }
                showsAppRepository.getDislikedShow(new DatabaseCallback<List<DislikedShow>>() {
                    @Override
                    public void onSuccess(List<DislikedShow> data) {
                        if(dataContainsDislikedShow(showId, data)){
                            showProgress();
                            ApiServiceFactory.get().likeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                @Override
                                public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                    if (response.isSuccessful()){
                                        ApiServiceFactory.get().likeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                            @Override
                                            public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                                if (response.isSuccessful()){
                                                    hideProgress();
                                                    likesCount.setText(String.valueOf(response.body().getLikesCount()));
                                                }else{
                                                    hideProgress();
                                                    showError("error");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                                hideProgress();
                                                showError("error");
                                            }
                                        });
                                    }else{
                                        hideProgress();
                                        showError("error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                    hideProgress();
                                    showError("error");
                                }
                            });


                        }else{
                            ApiServiceFactory.get().likeShow(((MainActivity)getActivity()).getUserToken(),show.getId()).enqueue(new Callback<ResponseLikeDislike>() {
                                @Override
                                public void onResponse(Call<ResponseLikeDislike> call, Response<ResponseLikeDislike> response) {
                                    if (response.isSuccessful()){
                                        hideProgress();
                                        likesCount.setText(String.valueOf(response.body().getLikesCount()));
                                    }else{
                                        hideProgress();
                                        showError("error");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseLikeDislike> call, Throwable t) {
                                    hideProgress();
                                    showError("error");
                                }
                            });
                        }
                        showsAppRepository.deleteDislikedShow(new DislikedShow(showId), new DatabaseCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                showsAppRepository.insertLikedShow(new LikedShow(showId), new DatabaseCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void data) {

                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        hideProgress();
                                        showError("error");
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable t) {
                                hideProgress();
                                showError("error");
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable t) {
                        hideProgress();
                        showError("error");
                    }
                });
            }
            @Override
            public void onError(Throwable t) {
                hideProgress();
                showError("error");
            }
        });
    }

    private boolean dataContainsDislikedShow(String showId, List<DislikedShow> data) {
        for (DislikedShow dislikedShow:data){
            if (dislikedShow.getId().equals(showId)){
                return true;
            }
        }
        return false;
    }

    private boolean dataContainsLikedShow(String showId, List<LikedShow> data) {
        for (LikedShow likedShow:data){
            if (likedShow.getId().equals(showId)){
                return true;
            }
        }
        return false;
    }

    private void setToolbar(View view) {
        episodeCount.setText(String.valueOf(show.getEpisodes().size()));
        showDescription.setText("    "+show.getDescription());
        collapsingToolbar.setTitle(show.getName());
        Glide.with(getContext()).load(Uri.parse(show.getPictureUrl())).into(imageView);
        animToolbar.setNavigationIcon(R.drawable.ic_shortcut_arrow_back);
        animToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ViewGroup mContentContainer = view.findViewById(R.id.viewGroup);
        mContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

    }

    private void checkIfEpisodesEmpty() {
        if (episodesShowing.size()==0){
            emptyImage.setVisibility(View.VISIBLE);
            noEpisodeText.setVisibility(View.VISIBLE);
        } else{
            emptyImage.setVisibility(View.GONE);
            noEpisodeText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        toolbar=listener.getToolbar();
        toolbar.setVisibility(View.GONE);
    }

    public static EpisodeSelectFragment newInstance(String showId) {
        EpisodeSelectFragment fragment = new EpisodeSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, showId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showId = getArguments().getString(ARG_NUMBER);
        }
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

    protected void showError(String text) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error!")
                .setMessage(text)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }


    public interface OnEpisodeFragmentInteractionListener {
        public void onEpisodeSelected(Episode episode);
    }
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
