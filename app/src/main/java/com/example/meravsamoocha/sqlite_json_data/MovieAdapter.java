package com.example.meravsamoocha.sqlite_json_data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder> {


    private Context mContext;
   private static Cursor mCursor;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public MovieAdapter(Context context, Cursor cursor){
    mContext = context;
    mCursor = cursor;


    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_item, parent,false );


        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
if (!mCursor.moveToPosition(position)){
    return;
}

        System.out.println("this is curser  :"+mCursor);

String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME));
String image = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
Double rating = mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
int year = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_YEAR));
String genre = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE));
String [] g = {""};
g[0] = genre;


Movie m_movie = new Movie(title,image,rating,year,g);


holder.t_movie_name.setText(title);
//holder.t_movie_rating.setText(rating.toString());
        holder.t_movie_rating.setText(String.valueOf(rating));
holder.t_movie_year.setText(String.valueOf(year));
holder.t_movie_genre.setText(genre);
if (image!=null){
        Picasso.get().load(image).fit().into(holder.t_movie_image);}


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    //if we update our database , we will need to create a new cursor
    public void swapCursor (Cursor newCursor){
        if (mCursor!=null){
            mCursor.close();
            //get rid of the old cursor...
        }
        mCursor =newCursor;
        if (newCursor!=null){

            notifyDataSetChanged();
            //update the recyclerview...
        }
    }



    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public TextView t_movie_name;
        public TextView t_movie_year;
        public TextView t_movie_rating;
        public TextView t_movie_genre;
        public ImageView t_movie_image;

        public MovieViewHolder(View itemView) {
            super(itemView);
            t_movie_name = itemView.findViewById(R.id.m_m_title);
            t_movie_year = itemView.findViewById(R.id.m_m_year);
            t_movie_rating = itemView.findViewById(R.id.m_m_rating);
            t_movie_genre = itemView.findViewById(R.id.m_m_genre);
            t_movie_image =itemView.findViewById(R.id.image_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }



}
