package com.example.ledeni56.showsapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ledeni56.showsapp.Activities.BasicActivity;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Adapters.CommentsAdapter;
import com.example.ledeni56.showsapp.Entities.Comment;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.CommentPost;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeCommentsFragment extends Fragment {
    private static final String ARG_EPISODE = "arg episode";
    private Episode episode;
    private ToolbarProvider listener;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<Comment> comments;
    private View emptyState;
    private View postButton;
    private ProgressDialog progressDialog;
    private EditText commentEditText;
    private ShowsAppRepository showsAppRepository;
    private BasicActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMyToolbar();
        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        emptyState = view.findViewById(R.id.emptyState);
        postButton = view.findViewById(R.id.postButton);
        commentEditText = view.findViewById(R.id.commentEditText);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        if (showsAppRepository == null) {
            showsAppRepository = ShowsAppRepository.get(getActivity());
        }

        initPostButton();
        getComments();
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
            }
        });
    }

    private void initPostButton() {
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isInternetAvailable()) {
                    if (!commentEditText.getText().toString().equals("")) {
                        activity.showProgress();
                        ApiServiceFactory.get().postComment(((MainActivity) getActivity()).getUserToken(), new CommentPost(commentEditText.getText().toString(), episode.getId())).enqueue(new Callback<ResponseData<Comment>>() {
                            @Override
                            public void onResponse(Call<ResponseData<Comment>> call, Response<ResponseData<Comment>> response) {
                                if (response.isSuccessful()) {
                                    comments.add(response.body().getData());
                                    recyclerView.getAdapter().notifyItemInserted(comments.size() - 1);
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    commentEditText.setText("");
                                    checkEmpty();
                                    activity.hideProgress();
                                    showsAppRepository.insertComments(comments, new DatabaseCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void data) {

                                        }

                                        @Override
                                        public void onError(Throwable t) {

                                        }
                                    });
                                } else {
                                    activity.showError("Error");
                                    activity.hideProgress();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<Comment>> call, Throwable t) {
                                activity.showError("Error");
                                activity.hideProgress();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "Internet is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getComments() {
        if (activity.isInternetAvailable()) {
            swipeRefreshLayout.setRefreshing(true);
            ApiServiceFactory.get().getComments(episode.getId()).enqueue(new Callback<ResponseData<List<Comment>>>() {
                @Override
                public void onResponse(Call<ResponseData<List<Comment>>> call, Response<ResponseData<List<Comment>>> response) {
                    if (response.isSuccessful()) {
                        comments = response.body().getData();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new CommentsAdapter(comments));
                        checkEmpty();
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                        showsAppRepository.insertComments(comments, new DatabaseCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {

                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        activity.showError("Error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<List<Comment>>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    activity.showError("Error");
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            showsAppRepository.getComments(new DatabaseCallback<List<Comment>>() {
                @Override
                public void onSuccess(List<Comment> data) {
                    comments = new ArrayList<>();
                    for (Comment comment : data) {
                        if (comment.getEpisodeId().equals(episode.getId())) {
                            comments.add(comment);
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new CommentsAdapter(comments));
                    checkEmpty();
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

    private void checkEmpty() {
        if (comments.size() == 0) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }


    private void setMyToolbar() {
        toolbar = listener.getToolbar();
        toolbar.getMenu().clear();
        toolbar.setTitle("Comments");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setVisibility(View.VISIBLE);
    }


    public static EpisodeCommentsFragment newInstance(Episode episode) {
        EpisodeCommentsFragment fragment = new EpisodeCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EPISODE, episode);
        fragment.setArguments(args);
        return fragment;
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
        activity = (BasicActivity) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            episode = getArguments().getParcelable(ARG_EPISODE);
        }
    }
}
