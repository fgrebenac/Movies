package com.fgrebenac.movies.data.api;

import com.fgrebenac.movies.data.models.Movie;
import com.fgrebenac.movies.data.models.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {

    @GET("movie/top_rated?api_key=fe3b8cf16d78a0e23f0c509d8c37caad")
    Call<MovieList> getTopRatedMovieList();

    @GET("movie/popular?api_key=fe3b8cf16d78a0e23f0c509d8c37caad")
    Call<MovieList> getPopularMovieList();

    @GET("movie/{movieId}?api_key=fe3b8cf16d78a0e23f0c509d8c37caad")
    Call<Movie> getMovieDetails(@Path("movieId") String movieId);

    @GET("movie/{movieId}/similar?api_key=fe3b8cf16d78a0e23f0c509d8c37caad")
    Call<MovieList> getSimilarMovies(@Path("movieId") String movieId);
}
