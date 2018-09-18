package com.fgrebenac.movies.data.models;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

public class MovieList {

    @Json(name = "results")
    private List<Movie> movies;

    public MovieList() {
    }

    public List<Movie> getMovies() {
        return movies != null ? movies : new ArrayList<Movie>();
    }
}
