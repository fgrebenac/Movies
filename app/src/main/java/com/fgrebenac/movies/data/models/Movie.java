package com.fgrebenac.movies.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class Movie {

    @Json(name = "id")
    private int id;
    @Json(name = "title")
    private String title;
    @Json(name = "poster_path")
    private String posterPath;
    @Json(name = "backdrop_path")
    private String coverPath;
    @Json(name = "overview")
    private String overview;
    @Json(name = "release_date")
    private String releaseDate;
    @Json(name = "genres")
    private List<Genre> genres;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
