package com.ubpatel.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovieDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POSTERL_URL + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_GENRE + " TEXT NOT NULL " + ");";

        final String SQL_CREATE_MOVIE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.MovieTrailerEntry.TABLE_NAME + " (" +

                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL, " +
                MovieContract.MovieTrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")" + ");";

        final String SQL_CREATE_MOVIE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.MovieReviewEntry.TABLE_NAME + " (" +

                MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.MovieReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")" + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_TRAILER_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("TEXT", "DROPPED DATABASE");
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieTrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
