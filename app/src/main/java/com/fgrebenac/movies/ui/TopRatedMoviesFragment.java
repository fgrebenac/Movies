package com.fgrebenac.movies.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fgrebenac.movies.R;
import com.fgrebenac.movies.data.api.ApiServiceFactory;
import com.fgrebenac.movies.data.models.Movie;
import com.fgrebenac.movies.data.models.MovieList;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trMoviesRecyclerView = view.findViewById(R.id.listRecyclerView);
        getTopRatedMoviesFromApi();
    }

    public static TopRatedMoviesFragment newInstance() {
        Bundle args = new Bundle();
        TopRatedMoviesFragment fragment = new TopRatedMoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getTopRatedMoviesFromApi() {
        getTopRatedMovieListCall = ApiServiceFactory.getApiService().getTopRatedMovieList();
        getTopRatedMovieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movies = response.body().getMovies();
                displayMovies();
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(getContext(), "Failed getting movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMovies() {
        if(!movies.isEmpty()) {
            trMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            moviesAdapter = new MoviesAdapter(movies, new MoviesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie item) {
                    startMovieDetailsFragment(item);
                }
            });
            trMoviesRecyclerView.setAdapter(moviesAdapter);
        }
    }

    private void startMovieDetailsFragment(Movie item) {

    }
}
