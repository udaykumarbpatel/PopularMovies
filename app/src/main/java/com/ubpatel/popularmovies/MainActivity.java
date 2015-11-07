package com.ubpatel.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MoviePosterFragment.CallBack {


    final static String EXTRA_BUNDLE = "PACKAGE";
    final static String EXTRA_DATABASE = "FALSE";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container_detail) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie, String isDatabase) {
        MovieDetailFragment detail_frag = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_detail);

        if (detail_frag != null) {
            detail_frag.receiveData(movie, isDatabase);
        } else {
            Intent intent = new Intent(getApplication(), MovieDetail.class);
            intent.putExtra(EXTRA_BUNDLE, movie);
            intent.putExtra(EXTRA_DATABASE, isDatabase);
            startActivity(intent);
        }
    }
}
