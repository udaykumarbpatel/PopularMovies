package com.ubpatel.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ukumar on 8/31/2015.
 */
public class Movie implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String movie_id;
    String original_title;
    String poster_image_url;
    String overview;
    String user_rating;
    String release_date;
    ArrayList<MovieTrailer> trailer_ids;
    ArrayList<MovieReview> movie_reviews;

    public Movie() {
    }

    public Movie(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.original_title = data[0];
        this.poster_image_url = data[1];
        this.overview = data[2];
        this.user_rating = data[3];

        this.release_date = data[4];
        this.movie_id = data[5];

    }

    public ArrayList<MovieReview> getMovie_reviews() {
        return movie_reviews;
    }

    public void setMovie_reviews(ArrayList<MovieReview> movie_reviews) {
        this.movie_reviews = movie_reviews;
    }

    public ArrayList<MovieTrailer> getTrailer_ids() {
        return trailer_ids;
    }

    public void setTrailer_ids(ArrayList<MovieTrailer> trailer_ids) {
        this.trailer_ids = trailer_ids;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_image_url() {
        return poster_image_url;
    }

    public void setPoster_image_url(String poster_image_url) {
        this.poster_image_url = poster_image_url;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.original_title,
                this.poster_image_url,
                this.overview,
                this.user_rating,
                this.release_date,
                this.movie_id
        });
    }

}
