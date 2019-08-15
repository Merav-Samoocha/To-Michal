package com.example.meravsamoocha.sqlite_json_data;

import android.provider.BaseColumns;

public class MovieContract {

    //Constructor
    private MovieContract(){};


    //Here we define the table's name and columns

    public static final class MovieEntry implements BaseColumns{

      public static final String TABLE_NAME="movieList";

        public static final String COLUMN_NAME="movie_title";
        public static final String COLUMN_IMAGE="movie_image";
        public static final String COLUMN_RATING="movie_rating";
        public static final String COLUMN_GENRE= "movie_genre";
        public static final String COLUMN_YEAR="movie_year";
    }
}

