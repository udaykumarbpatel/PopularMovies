package com.ubpatel.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubpatel.popularmovies.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDBAccess {
    SQLiteDatabase db;

    public MovieDBAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginal_title());
        values.put(MovieContract.MovieEntry.COLUMN_POSTERL_URL, movie.getPoster_image_url());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getUser_rating());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE, movie.getMovie_genre());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        long rowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        return rowId >= 0;
    }

    public boolean delete(String movie_id) {
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + movie_id, null) > 0;
    }

    public boolean get(String movieid) {
        Cursor c = db.query(true, MovieContract.MovieEntry.TABLE_NAME, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + movieid, null, null, null, null, null);
        if (c.getCount() > 0) {
            return true;
        }
        if (!c.isClosed()) {
            c.close();
        }
        return false;
    }

    public List<Movie> getAll() {
        List<Movie> all_movies = new ArrayList<>();
        Cursor c = db.query(true, MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                Movie movie = this.buildMovieFromCursor(c);
                if (movie != null) {
                    all_movies.add(movie);
                }
            } while (c.moveToNext());

            if (!c.isClosed()) {
                c.close();
            }
        }
        return all_movies;
    }

    private Movie buildMovieFromCursor(Cursor cursor) {
        Movie movie = null;
        if (cursor != null) {
            movie = new Movie();
            movie.setMovie_id(cursor.getString(0));
            movie.setOriginal_title(cursor.getString(1));
            movie.setPoster_image_url(cursor.getString(2));
            movie.setOverview(cursor.getString(3));
            movie.setUser_rating(cursor.getString(4));
            movie.setRelease_date(cursor.getString(5));
            movie.setMovie_genre(cursor.getString(6));
        }
        return movie;
    }
}
