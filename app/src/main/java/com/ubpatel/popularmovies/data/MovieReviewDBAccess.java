package com.ubpatel.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubpatel.popularmovies.Movie;
import com.ubpatel.popularmovies.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewDBAccess {

    SQLiteDatabase db;

    public MovieReviewDBAccess(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean insert(Movie movie) {
        for (int i = 0; i < movie.getMovie_reviews().size(); i++) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
            values.put(MovieContract.MovieReviewEntry.COLUMN_AUTHOR, movie.getMovie_reviews().get(i).getAuthor());
            values.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, movie.getMovie_reviews().get(i).getContent());
            long rowId = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, values);
            if (rowId < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean delete(String movie_id) {
        return db.delete(MovieContract.MovieReviewEntry.TABLE_NAME, MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + "=" + movie_id, null) > 0;
    }

    public List<MovieReview> getAll(Movie movie) {
        List<MovieReview> all_reviews = new ArrayList<>();
        Cursor c = db.query(true, MovieContract.MovieReviewEntry.TABLE_NAME, null, MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + "=" + movie.getMovie_id(), null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            do {
                MovieReview movieReview = this.buildMovieFromCursor(c);
                all_reviews.add(movieReview);
            } while (c.moveToNext());

            if (!c.isClosed()) {
                c.close();
            }
        }
        return all_reviews;
    }

    private MovieReview buildMovieFromCursor(Cursor cursor) {
        MovieReview movie = null;
        if (cursor != null) {
            movie = new MovieReview();
            movie.setAuthor(cursor.getString(1));
            movie.setContent(cursor.getString(2));
        }
        return movie;
    }
}
