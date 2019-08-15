package com.example.meravsamoocha.sqlite_json_data;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Activity4 extends AppCompatActivity implements MovieAdapter.OnItemClickListener  {


    Button btn4_add;
    RecyclerView rv4;
    private RelativeLayout rl4;
    private RequestQueue m4_requestq;

    private SQLiteDatabase m4Database, m5Database;
    private MovieAdapter mAdapter4;
    MovieDBHelper m4_helper, m_helper;



    // For the detail activity
    public static final String EXTRA_URL = "imageUrl_d";
    public static final String EXTRA_TITLE = "titleName_d";
    public static final String EXTRA_YEAR = "year_d";
    public static final String EXTRA_RATING = "rating_d";
    public static final String EXTRA_GENRE = "genre_d";

    //Variables For the result added item
    String s = "";


    String t4_title ;
    int t4_year ;
    Double t4_rating;
    String t4_image  ;
    String t4_genre;
    MovieDBHelper dbHelper;

    public ArrayList<Movie> themovielist4=null;

    private static boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

    btn4_add=findViewById(R.id.btn4_add_now);
    rv4 = findViewById(R.id.recyclerview4);
    rl4 = findViewById(R.id.thebiglayout4);

    dbHelper = new MovieDBHelper(Activity4.this);
        m4Database = dbHelper.getWritableDatabase();
        m5Database = dbHelper.getReadableDatabase();


        m4_requestq= Volley.newRequestQueue(Activity4.this);

        if (firstRun){
            System.out.println("*******     this is first run true   *************");
            //Here we will get json just once into sqllitedatabase
            parseJson4();
        }
        firstRun=false;


       // parseJson4();
        //m_helper= new MovieDBHelper(this);

        //Try to initialize again


        themovielist4 = new ArrayList<>();



       // themovielist4= dbHelper.getMovieArray();
       // m_helper= new MovieDBHelper(this);

      //  themovielist4= m_helper.getMovieArray();



        rv4.setLayoutManager(new LinearLayoutManager(Activity4.this));
        mAdapter4 = new MovieAdapter(Activity4.this,getAllItems());
        rv4.setAdapter(mAdapter4);

        themovielist4= dbHelper.getMovieArray();
        System.out.println("*******     this is after oncreate initialize arrayList after adapter - the size is  *************"+ themovielist4.size());
        mAdapter4.setOnItemClickListener(Activity4.this);


        //Here Create an Integrator for the camera inside on create

        final IntentIntegrator i4 = new IntentIntegrator(Activity4.this);
        i4.setBeepEnabled(true);
        i4.setCameraId(0);

        btn4_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //here go to scanner to add
                //here for scanner
                i4.initiateScan(); //open camera


                  System.out.println("this is listener was pressed");

            }
        });




    }

    private void parseJson4() {
        String url4 =
                "https://api.androidhive.info/json/movies.json";
/*
"title": "Dawn of the Planet of the Apes",
        "image": "https://api.androidhive.info/json/movies/1.jpg",
        "rating": 8.3,
        "releaseYear": 2014,
        "genre": ["Action", "Drama", "Sci-Fi"]
*/

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url4, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{



                    //JSONArray jsonarray = response.getJSONArray(response);

                    for(int i=0; i<response.length(); i++){

                        System.out.println("this is the for loop:   "+i);
                        JSONObject ff = response.getJSONObject(i);

                        String m_title = ff.getString("title");
                        String m_image = ff.getString("image");
                        Double m_rating = ff.getDouble("rating");
                        int m_year = ff.getInt("releaseYear");

                        JSONArray genresArray=ff.getJSONArray("genre");
                        String[]genres=new String[genresArray.length()];

                        StringBuilder builder = new StringBuilder();

                        String line = null;
                        for (int j = 0; j < genresArray.length(); j++) {
                            genres[j]=genresArray.getString(j);

                            line=genres[j];
                            if(j!=(genresArray.length()-1)){

                                builder.append(line+", ");


                            }
                            else {
                                builder.append(line+".");

                            }




                            line = builder.toString();


                        }
                        System.out.println("this is one line");
                        System.out.println("  "+m_title+ " "+ m_image+"  "+m_year);
                        System.out.println("this is the array string:  "+ line);
                        Movie m = new Movie(m_title,m_image,m_rating,m_year,genres);
                        //here insert to sql table
                       ContentValues cv = new ContentValues();
                        cv.put(MovieContract.MovieEntry.COLUMN_NAME, m_title);

                        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE, m_image);
                        cv.put(MovieContract.MovieEntry.COLUMN_GENRE, line);
                        cv.put(MovieContract.MovieEntry.COLUMN_RATING, m_rating);
                        cv.put(MovieContract.MovieEntry.COLUMN_YEAR, m_year);

                        m4Database.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
                        mAdapter4.swapCursor(getAllItems());

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        m4_requestq.add(request);
    }


    private Cursor getAllItems(){
        //sort by movie year
        return m4Database.query(MovieContract.MovieEntry.TABLE_NAME,null,null, null,null,null,MovieContract.MovieEntry.COLUMN_YEAR+ " DESC");
    }

    @Override
    public void onItemClick(int position) {
        System.out.println("item was clicked"+ position);

        if (position >=0){
            // themovielist = MovieAdapter.getMovieArray(position);


            Intent detailIntent = new Intent(Activity4.this, DetailActivity.class);

            Cursor c2 = getAllItems();
            if (!c2.moveToPosition(position)){
                System.out.println("this is return, position:"+ position);

                return;}
            else {
                System.out.println("this is else, position:"+ position);
                String t = c2.getString(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
                System.out.println("this is else, title:"+ t);

                //here create item again
                String title = c2.getString(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
                String image = c2.getString(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
                Double rating = c2.getDouble(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
                int year = c2.getInt(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR));
                String genre = c2.getString(c2.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE));
                String[] g = {""};
                g[0] = genre;
                System.out.println("this is else, title:"+ t+ "  "+image+"  "+rating+"  "+year+"  "+ " "+g[0]);

                Movie m_movie = new Movie(title, image, rating, year, g);

                detailIntent.putExtra(EXTRA_URL, m_movie.getImage());
                detailIntent.putExtra(Intent.EXTRA_TITLE,title);
                detailIntent.putExtra(EXTRA_TITLE, m_movie.getTitle());
                detailIntent.putExtra(EXTRA_YEAR, m_movie.getReleaseyear());
                detailIntent.putExtra(EXTRA_RATING, m_movie.getRating());
                detailIntent.putExtra(EXTRA_GENRE, m_movie.genre[0]);

                System.out.println("*******     this is inside on click item arrayList - the size is  *************"+ themovielist4.size());
            }


            startActivity(detailIntent);}
    }

    public void showsnackbar(){
        Snackbar snb = Snackbar.make(rl4,"The movie already exists in the database", Snackbar.LENGTH_LONG);


        snb.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result!=null){

            if( result.getContents()!= null) {
                System.out.println("the result is not null");
                s = result.getContents().toString();
                System.out.println("this is the result:   " + s);


                //Try to turn plain string into Movie object
                Gson g = new Gson();
                final Movie trymovie = g.fromJson(s,Movie.class);

//third try just to get the correct year because gson is wrong

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = null;
                try {
                    node = mapper.readTree(s);
                    t4_title = node.get("title").asText();
                    t4_year = node.get("releaseYear").asInt();
                    t4_rating = node.get("rating").asDouble();
                    t4_image = node.get("image").asText();
                    // String t2_genre= node.get("genre").asText();

                    System.out.println("This is try2 movie from node: "+t4_title);
                    System.out.println("This is try2 movie year from node: "+t4_year);
                    System.out.println("This is try2 movie rating from node: "+t4_rating);
                    System.out.println("This is try2 movie image from node: "+t4_image);
                    // System.out.println("This is try2 movie genre from node: "+t2_genre);

                } catch (IOException e) {
                    e.printStackTrace();
                }



                if (trymovie == null){
                    System.out.println("try movie is null ");
                }
//the movie is not null
                else {

                    //System.out.println("release year from GSON:   " + y);
                    String q_title = trymovie.getTitle();
                    System.out.println("inside qqq title:   " + q_title);
                    System.out.println("inside qqq year:   " + trymovie.getReleaseyear());
                    System.out.println("inside qqq rating:   " + trymovie.getRating());
                    System.out.println("this is the genre    " +  trymovie.getGenre());
                    String []g5 = trymovie.getGenre();
                    System.out.println("this is the genre  0+1  " +  g5[0]+g5[1]);

                    System.out.println("this is the length of array list  " + themovielist4.size());


                    //Try search without arrayList Just with cursor
                    Cursor c4 = getAllItems();
                    System.out.println("this is c4 get position  " +c4.getPosition());
                    System.out.println("this is c4 move to next  " +c4.moveToNext());
                    System.out.println("this is c4 move to first  " +c4.moveToFirst());
                    Boolean was_found = false;

                    c4.moveToFirst();
                    while (c4.moveToNext()){
                        String title4 = c4.getString(c4.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
                        if (q_title.equals(title4)){
                            //the movie is already in the list
                            System.out.println("the item was found    "+c4.getPosition());
                            was_found=true;
                            showsnackbar();
                            break;
                        }
                        else{
                            //the item wasn't found continue while loop
                            System.out.println("the item wasn't found    "+c4.getPosition());

                        }

                    }
                    //the item wasn't  found in all database...add it
                    if(!was_found){
                        //We will add item here
                        System.out.println("the item wasn't found   we will add it to the list ");
                        // add the item to database
                        Movie m6 = new Movie(t4_title,t4_image,t4_rating,t4_year, trymovie.getGenre());
                        additem(m6);
                    }


//now we search for it in the array
                 /*   for (int i=0; i< themovielist4.size(); i++){
                        Movie m5= themovielist4.get(i);
                        System.out.println("this is the for search loop    "+i+"   "+m5.getTitle());
                        System.out.println("this is the for arraylength    "+themovielist4.size());


                        if (q_title.equals(m5.title) ){
                            System.out.println("the item was found    "+i+"   "+m5.getTitle());

                            //the movie is already in the list, We need to break for loop and show snackbar


                            showsnackbar();
                            break;
                        }
                        else if ( i == themovielist4.size()-1){
                            System.out.println("*******    inside if else of for loop"+ themovielist4.size());
                            System.out.println("the item wasn't found");
                            //the movie was not found
                            // add the item to database
                            Movie m6 = new Movie(t4_title,t4_image,t4_rating,t4_year, trymovie.getGenre());
                            additem(m6);
                            //here update the array
                            //here update cursor, already updated in add item
                            System.out.println("this is after the for loop    "+i+"   "+m5.getTitle());
                            System.out.println("the movie wasn't found    "+i+"   "+q_title+"  Will be added to the database");
                            //break;
                        }
                    }
*/

                }


            }


            // result is null
            else
            {
                System.out.println("\n the result is null");

            }
        }

    }

    public void additem (Movie m){


        // Movie m = new Movie(m_title,m_image,m_rating,m_year,genres);
        String m_title = m.getTitle();
        String pppp= m.getImage();
   /* StringBuilder sbb = new StringBuilder();
    sbb.append(pppp+".jpg");

    String m_image = sbb.toString();
    */

        String []line =m.getGenre();

        StringBuilder builder = new StringBuilder();

        String l = null;
        for (int j = 0; j < line.length; j++) {


            l=line[j].toString();
            if(j!=(line.length-1)){

                builder.append(l+", ");


            }
            else {
                builder.append(l+".");

            }




            l = builder.toString();
            System.out.println("this is thefor loop for the genre String:   " + j);

        }

        System.out.println("this is the string of gener:   " + l);




        Double m_rating = m.getRating();
        Integer m_year = m.getReleaseyear();
        System.out.println("this is the release year:   " + m_year);


        //here insert to sql table
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_NAME, m_title);

        cv.put(MovieContract.MovieEntry.COLUMN_IMAGE, pppp);
        cv.put(MovieContract.MovieEntry.COLUMN_GENRE, l);
        cv.put(MovieContract.MovieEntry.COLUMN_RATING, m_rating);
        cv.put(MovieContract.MovieEntry.COLUMN_YEAR, m_year);

        m4Database.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        mAdapter4.swapCursor(getAllItems());

//Update Array
      //  themovielist4=null;
      //  themovielist4= dbHelper.getMovieArray();

    }


}
