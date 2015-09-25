package com.ubpatel.popularmovies;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ukumar on 8/31/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private List<Movie> movies = new ArrayList<Movie>();
    private Activity activity;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        this.movies = movies;
        this.activity = context;
    }

    public int getCount() {
        return movies.size();
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);

        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(getContext()).cancelRequest(imageView);
        Picasso.with(getContext())
                .load(POSTER_BASE_URL + movies.get(position).getPoster_image_url())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(imageView);
        return imageView;
    }

    public static class ViewHolder {
        public ImageView movie_poster;
    }
}
