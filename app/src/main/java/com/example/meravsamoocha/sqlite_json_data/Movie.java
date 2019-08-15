package com.example.meravsamoocha.sqlite_json_data;

import java.util.Arrays;

public class Movie {

    //Properties
    String title;
    String image;
    Double rating;
    int releaseyear;
    String []genre;

    //Constructor
    public Movie(String title, String image, Double rating, int releaseyear, String []genre){

        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseyear = releaseyear;
        this.genre = genre;
    }


    public String getTitle(){return title;}

    public String getImage() { return image; }

    public Double getRating() { return rating; }
    public  int getReleaseyear() { return releaseyear;}

    public String[] getGenre() { return genre; }

    @Override
    public String toString() {
        return super.toString();



    }
}
