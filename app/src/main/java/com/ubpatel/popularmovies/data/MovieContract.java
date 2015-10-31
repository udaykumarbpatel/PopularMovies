package com.ubpatel.popularmovies.data;

public class MovieContract {

    public static final class MovieTrailerEntry {

        public static final String TABLE_NAME = "movietrailer";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_TRAILER_KEY = "tailer_key";

    }

    public static final class MovieReviewEntry {

        public static final String TABLE_NAME = "moviereview";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "review_author";
        public static final String COLUMN_CONTENT = "review_content";

    }

    public static final class MovieEntry {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTERL_URL = "poster_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_GENRE = "genre";

    }

}
