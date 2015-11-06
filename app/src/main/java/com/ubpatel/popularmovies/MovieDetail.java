package com.ubpatel.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container_detail, new MovieDetailFragment())
//                .commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
