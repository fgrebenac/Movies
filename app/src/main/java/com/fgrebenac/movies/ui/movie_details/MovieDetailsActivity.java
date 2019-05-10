package com.fgrebenac.movies.ui.movie_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fgrebenac.movies.R;
import com.fgrebenac.movies.data.api.ApiServiceFactory;
import com.fgrebenac.movies.data.models.Genre;
import com.fgrebenac.movies.data.models.Movie;
import com.fgrebenac.movies.data.models.MovieList;
import com.fgrebenac.movies.utils.BaseActivity;
import com.fgrebenac.movies.ui.movies.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieCoverImageView;
    private ImageView moviePosterImageView;
    private TextView movieTitleTextView;
    private TextView releaseDateTextView;
    private TextView movieGenresTextView;
    private TextView movieOverviewTextView;
    private LinearLayout noMoviesLayout;
    private Call<Movie> getMovieDetailsCall;
    private Call<MovieList> getSimilarMoviesCall;
    private String movieId;
    private RecyclerView similarMoviesRecyclerView;
    private List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        setContentView(R.layout.activity_movie_details);
        movieId = getIntent().getStringExtra("movieId");
        initViews();
        getMovieDetailsFromApi();
        getSimilarMoviesFromApi();
    }

    private void initViews() {
        movieCoverImageView = findViewById(R.id.movieCoverImageView);
        moviePosterImageView = findViewById(R.id.moviePosterImageView);
        movieTitleTextView = findViewById(R.id.titleTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        movieGenresTextView = findViewById(R.id.genresTextView);
        movieOverviewTextView = findViewById(R.id.movieOverviewTextView);
        similarMoviesRecyclerView = findViewById(R.id.similarMoviesRecyclerView);
        noMoviesLayout = findViewById(R.id.noMoviesLayout);
    }

    private void getMovieDetailsFromApi() {
        BaseActivity.showProgress(MovieDetailsActivity.this, "Loading");
        getMovieDetailsCall = ApiServiceFactory.getApiService().getMovieDetails(movieId);
        getMovieDetailsCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(getSimilarMoviesCall.isExecuted()) {
                    BaseActivity.hideProgress();
                }
                Movie movie = response.body();
                Glide.with(movieCoverImageView)
                        .load( "https://image.tmdb.org/t/p/w400" + movie.getCoverPath())
                        .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_local_movies_24dp))
                        .apply(RequestOptions.centerCropTransform())
                        .into(movieCoverImageView);
                Glide.with(moviePosterImageView)
                        .load( "https://image.tmdb.org/t/p/w400" + movie.getPosterPath())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_local_movies_24dp))
                        .into(moviePosterImageView);
                movieTitleTextView.setText(movie.getTitle());
                Log.i("movietitle", movie.getTitle());
                releaseDateTextView.setText(movie.getReleaseDate());
                StringBuilder stringBuilder = new StringBuilder();
                for(Genre genre : movie.getGenres()) {
                    stringBuilder.append(genre.getName()).append(" ");
                }
                movieGenresTextView.setText(stringBuilder);
                movieOverviewTextView.setText(movie.getOverview());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                if(getSimilarMoviesCall.isExecuted()) {
                    BaseActivity.hideProgress();
                }
                Toast.makeText(MovieDetailsActivity.this, "Failed getting movie details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSimilarMoviesFromApi() {
        getSimilarMoviesCall = ApiServiceFactory.getApiService().getSimilarMovies(movieId);
        getSimilarMoviesCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                BaseActivity.hideProgress();
                movies = response.body().getMovies();
                displayMovies();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                BaseActivity.hideProgress();
                Toast.makeText(MovieDetailsActivity.this, "Failed getting similar movies.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayMovies() {
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(MovieDetailsActivity.this,
                R.anim.anim_layout_list);
        similarMoviesRecyclerView.setLayoutAnimation(layoutAnimationController);
        if(!movies.isEmpty()) {
            similarMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this));
            MoviesAdapter moviesAdapter = new MoviesAdapter(movies, new MoviesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Movie item) {
                    startMovieDetailsActivity(item);
                }
            }, true);
            similarMoviesRecyclerView.setAdapter(moviesAdapter);
        } else {
            noMoviesLayout.setVisibility(View.VISIBLE);
        }
    }

    private void startMovieDetailsActivity(Movie item) {
        Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
        intent.putExtra("movieId", String.valueOf(item.getId()));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    public void onStop() {
        try {
            if(!getSimilarMoviesCall.isExecuted()) getSimilarMoviesCall.cancel();
            if(!getMovieDetailsCall.isExecuted()) getMovieDetailsCall.cancel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
