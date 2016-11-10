package com.example.abdullah.getimdbmoviedetailsapp;

/**
 * Created by Abdullah on 11/9/2016.
 */

public class aMovie {
    private String id;
    private String title;
    private String year;
    private String director;
    private String rating;

    public aMovie(String id, String name, String year, String director, String rating) {
        this.id = id;
        this.title = name;
        this.year = year;
        this.director = director;
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
