package com.fgrebenac.movies.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.fgrebenac.movies.R;
import com.fgrebenac.movies.data.api.ApiServiceFactory;
import com.fgrebenac.movies.data.models.Movie;
import com.fgrebenac.movies.data.models.MovieList;
import com.fgrebenac.movies.utils.BaseActivity;
import com.fgrebenac.movies.ui.movie_details.MovieDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedMoviesFragment extends Fragment {

    private RecyclerView trMoviesRecyclerView;
    private MoviesAdapter moviesAdapter;
    private Call<MovieList> getTopRatedMovieListCall;
    private List<Movie> movies = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initSwipeRefreshLayout();
        getTopRatedMoviesFromApi();
    }

    public static TopRatedMoviesFragment newInstance() {
        Bundle args = new Bundle();
        TopRatedMoviesFragment fragment = new TopRatedMoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(View view) {
        trMoviesRecyclerView = view.findViewById(R.id.listRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTopRatedMoviesFromApi();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setDistanceToTriggerSync(20);
    }

    private void getTopRatedMoviesFromApi() {
        getTopRatedMovieListCall = ApiServiceFactory.getApiService().getTopRatedMovieList();
        getTopRatedMovieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                swipeRefreshLayout.setRefreshing(false);
                movies = response.body().getMovies();
                displayMovies();
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Failed getting movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMovies() {
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.anim_layout_fall_down);
        trMoviesRecyclerView.setLayoutAnimation(animationController);
        if(!movies.isEmpty()) {
            trMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            moviesAdapter = new MoviesAdapter(movies, new MoviesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie item) {
                    startMovieDetailsActivity(item);
                }
            }, false);
            trMoviesRecyclerView.setAdapter(moviesAdapter);
        }
    }

    private void startMovieDetailsActivity(Movie item) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("movieId", String.valueOf(item.getId()));
        startActivity(intent);
    }

    @Override
    public void onStop() {
        try {
            if(!getTopRatedMovieListCall.isExecuted()) getTopRatedMovieListCall.cancel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
