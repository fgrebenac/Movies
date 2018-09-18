package com.fgrebenac.movies.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fgrebenac.movies.R;
import com.fgrebenac.movies.data.models.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener itemClickListener;
    private boolean isList;

    public MoviesAdapter(List<Movie> movies, OnItemClickListener itemClickListener, boolean isList) {
        this.movies = movies;
        this.itemClickListener = itemClickListener;
        this.isList = isList;
    }

    @NonNull
    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                !isList ? R.layout.item_movie : R.layout.item_list_movie, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        final Movie movie = movies.get(position);
        ImageView movieImageView = movieViewHolder.itemView.findViewById(R.id.movieImageView);
        TextView movieTitleTextView = movieViewHolder.itemView.findViewById(R.id.movieTitleTextView);
        Glide.with(movieImageView)
                .load( "https://image.tmdb.org/t/p/w400" + movie.getPosterPath())
                .into(movieImageView);
        movieTitleTextView.setText(movie.getTitle());
        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener != null) {
                    itemClickListener.onItemClick(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Movie item);
    }
}
