package com.example.meravsamoocha.sqlite_json_data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.meravsamoocha.sqlite_json_data.Activity4.EXTRA_GENRE;
import static com.example.meravsamoocha.sqlite_json_data.Activity4.EXTRA_RATING;
import static com.example.meravsamoocha.sqlite_json_data.Activity4.EXTRA_TITLE;
import static com.example.meravsamoocha.sqlite_json_data.Activity4.EXTRA_URL;
import static com.example.meravsamoocha.sqlite_json_data.Activity4.EXTRA_YEAR;

public class DetailActivity extends AppCompatActivity {
TextView m_t, m_r, m_y, m_g;
ImageView m_i_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent i2 = getIntent();

      String d_url = i2.getStringExtra(EXTRA_URL);
       int d_year = i2.getIntExtra(EXTRA_YEAR,0);
     Double d_rate = i2.getDoubleExtra(EXTRA_RATING, 0);
       String d_genre = i2.getStringExtra(EXTRA_GENRE);
        String d_title = i2.getStringExtra(EXTRA_TITLE);

        System.out.println("this is title from detail activity:  "+ d_title);
        m_t = findViewById(R.id.text_view_title_detail);
        m_r = findViewById(R.id.text_view_rating_detail);
        m_y = findViewById(R.id.text_view_year_detail);
        m_g =findViewById(R.id.text_view_genre_detail);
        m_i_v= findViewById(R.id.image_view_detail);


        m_t.setText(d_title);
      m_g.setText(d_genre);
        m_y.setText("Release Year:  " +String.valueOf(d_year));
       m_r.setText("Rating:  "+String.valueOf(d_rate));
       Picasso.get().load(d_url).fit().into(m_i_v);










    }
}
