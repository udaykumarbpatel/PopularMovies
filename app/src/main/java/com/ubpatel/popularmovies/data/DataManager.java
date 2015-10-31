package com.ubpatel.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ubpatel.popularmovies.Movie;

public class DataManager {
    Context mContext;
    MovieDBHelper movieDBHelper;
    SQLiteDatabase db;
    MovieDBAccess movieDAO;
    MovieReviewDBAccess movieReviewDAO;
    MovieTrailerDBAccess movieTrailerDAO;

    public DataManager(Context mContext) {
        this.mContext = mContext;
        movieDBHelper = new MovieDBHelper(mContext);
        db = movieDBHelper.getWritableDatabase();
        movieDAO = new MovieDBAccess(db);
        movieReviewDAO = new MovieReviewDBAccess(db);
        movieTrailerDAO = new MovieTrailerDBAccess(db);
    }

    public void close() {
        db.close();
    }

    public boolean insertMovie(Movie movie) {
        return movieDAO.insert(movie) && movieTrailerDAO.insert(movie) && movieReviewDAO.insert(movie);
    }

    public boolean deleteMovie(Movie movie) {
        return movieTrailerDAO.delete(movie.getMovie_id()) && movieReviewDAO.delete(movie.getMovie_id()) && movieDAO.delete(movie.getMovie_id());
    }

    public boolean getFavorite(String movie_id) {
        return movieDAO.get(movie_id);
    }

}
