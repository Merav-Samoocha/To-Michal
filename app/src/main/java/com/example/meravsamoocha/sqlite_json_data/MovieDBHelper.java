package com.example.meravsamoocha.sqlite_json_data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "movielist.db";
    public static final int DATABASE_VERSION= 1;

    //Constructor
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQLCREATE_MOVIELIST_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, "+
                MovieContract.MovieEntry.COLUMN_RATING + " DOUBLE NOT NULL, "+
                MovieContract.MovieEntry.COLUMN_YEAR + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_GENRE + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQLCREATE_MOVIELIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }




    //a function which returns an arrayList of Movies from the SQLite...added for the search
//return Movie object
    public  ArrayList<Movie> getMovieArray(){

        ArrayList<Movie> themovielist1= new ArrayList<>();;
        SQLiteDatabase database3 = this.getReadableDatabase();
        Cursor ccc = database3.rawQuery("Select * from "+MovieContract.MovieEntry.TABLE_NAME,null);

        System.out.println("*******     this is inside getMovie array before if   *************");
        if (ccc!=null)
        {
            System.out.println("*******     this is inside getMovie array inside if cursor !=null   ************* cc position is:"+ccc.getPosition());
            ccc.moveToFirst();
            if (ccc.moveToFirst()){
                //the cursor is not empty
                System.out.println("the cursur is EMPTY _______");

            }
            else
                //the cursor is empty
                {System.out.println("the cursur is NOT EMPTY _______");}
           // String title555 = ccc.getString(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
           // System.out.println("*******     this is title555 of cursor move to first   "+ title555);
           // ccc.moveToPosition(1);
            if (!ccc.moveToNext()){
                //move to next is not null
               System.out.println("*******     move to next is  null");
            }

            System.out.println("*******     this is inside getMovie array inside if cursor !=null   ************* cc position after move to first is:"+ccc.getPosition());
            while (ccc.moveToNext()){
                String title = ccc.getString(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
                String image = ccc.getString(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
                Double rating = ccc.getDouble(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
                int year = ccc.getInt(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR));
                String genre = ccc.getString(ccc.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE));
                String [] g = {""};
                g[0] = genre;

                Movie m_movie = new Movie(title,image,rating,year,g);

                themovielist1.add(m_movie);

                System.out.println("*******     this is inside getMovie array inside if cursor !=null  inside while *************");
            }
        }
        ccc.close();
        return themovielist1;
    }


}
