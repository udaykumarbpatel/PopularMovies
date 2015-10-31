package com.ubpatel.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubpatel.popularmovies.Movie;
import com.ubpatel.popularmovies.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerDBAccess {
    SQLiteDatabase db;

    public MovieTrailerDBAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(Movie movie) {

        for (int i = 0; i < movie.getTrailer_ids().size(); i++) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
            values.put(MovieContract.MovieTrailerEntry.COLUMN_TRAILER_KEY, movie.getTrailer_ids().get(i).getTrailer_key());
            values.put(MovieContract.MovieTrailerEntry.COLUMN_TRAILER_NAME, movie.getTrailer_ids().get(i).getTrailer_name());
            long rowId = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, values);
            if (rowId < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean delete(String movie_id) {
        return db.delete(MovieContract.MovieTrailerEntry.TABLE_NAME, MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + "=" + movie_id, null) > 0;
    }

    public List<MovieTrailer> getAll(Movie movie) {
        List<MovieTrailer> all_trailers = new ArrayList<>();
        Cursor c = db.query(true, MovieContract.MovieTrailerEntry.TABLE_NAME, null, MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + "=" + movie.getMovie_id(), null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                MovieTrailer movieTrailer = this.buildMovieFromCursor(c);
                all_trailers.add(movieTrailer);
            } while (c.moveToNext());

            if (!c.isClosed()) {
                c.close();
            }
        }
        return all_trailers;
    }

    private MovieTrailer buildMovieFromCursor(Cursor cursor) {
        MovieTrailer movie = null;
        if (cursor != null) {
            movie = new MovieTrailer();
            movie.setTrailer_key(cursor.getString(1));
            movie.setTrailer_name(cursor.getString(2));
        }
        return movie;
    }
}
