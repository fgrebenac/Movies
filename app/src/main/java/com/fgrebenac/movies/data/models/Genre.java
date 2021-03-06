package com.fgrebenac.movies.data.models;

import com.squareup.moshi.Json;

public class Genre {

    @Json(name = "id")
    private int id;
    @Json(name = "name")
    private String name;

    public Genre() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
