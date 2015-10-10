package com.ubpatel.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle data = getIntent().getExtras();

        Movie movie = data.getParcelable(MoviePosterFragment.EXTRA_BUNDLE);

        TextView movie_title = (TextView) findViewById(R.id.original_title);
        TextView synopsis = (TextView) findViewById(R.id.overview);
        RatingBar vote_average = (RatingBar) findViewById(R.id.ratingBar);
        TextView release_date = (TextView) findViewById(R.id.release_date);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);


        imageView.setAdjustViewBounds(true);
        Picasso.with(this)
                .load(POSTER_BASE_URL + movie.getPoster_image_url())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(imageView);

        movie_title.setText(movie.getOriginal_title());
        synopsis.setText(movie.getOverview());
        vote_average.setRating((Float.parseFloat(movie.getUser_rating()) * 5) / 10);
        if (movie.getRelease_date().equals("null")) {
            release_date.setText("Not Available");
        } else {
            String date = movie.getRelease_date();
            String[] year = date.split("-");
            release_date.setText(year[0]);
        }


    }
}
