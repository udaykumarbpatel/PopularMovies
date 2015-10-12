package com.ubpatel.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    final String LOG_TAG = "Log Msg : ";
    Movie movie;
    ListView trailer_list;
    ListView review_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle data = getIntent().getExtras();

        movie = data.getParcelable(MoviePosterFragment.EXTRA_BUNDLE);

        TextView movie_title = (TextView) findViewById(R.id.original_title);
        TextView synopsis = (TextView) findViewById(R.id.overview);
        RatingBar vote_average = (RatingBar) findViewById(R.id.ratingBar);
        TextView release_date = (TextView) findViewById(R.id.release_date);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        trailer_list = (ListView) findViewById(R.id.listView);
        review_list = (ListView) findViewById(R.id.listView_review);

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

        Log.d("Movie ID: ", movie.getMovie_id());
        FetchTrailerTask movieTask = new FetchTrailerTask();
        movieTask.execute(movie.getMovie_id());

        FetchReviewTask reviewTask = new FetchReviewTask();
        reviewTask.execute(movie.getMovie_id());

    }

    private void setTrailerForAdapter(final Movie result) {
        trailer_list.setEmptyView(findViewById(R.id.empty_list_view));
        trailer_list.setAdapter(new TrailerAdapter(getApplicationContext(), result.getTrailer_ids()));
        trailer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + result.getTrailer_ids().get(position).getTrailer_key()));
                startActivity(intent);
            }
        });

    }

    private void setMovieReviewForAdapter(final Movie result) {

        for (int i = 0; i < result.getMovie_reviews().size(); i++) {
            Log.d("Movie.Info : ", movie.getMovie_reviews().get(i).getAuthor());
        }

        review_list.setEmptyView(findViewById(R.id.empty_list_view_review));
        review_list.setAdapter(new ReviewAdapter(getApplicationContext(), result.getMovie_reviews()));

//        review_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + result.getTrailer_ids().get(position).getTrailer_key()));
//                startActivity(intent);
//            }
//        });

    }

    public class FetchTrailerTask extends AsyncTask<String, Void, Movie> {

        private Movie getMovieTrailerFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_KEY = "key";
            final String OWM_NAME = "name";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            ArrayList<MovieTrailer> all_trailers = new ArrayList<>();


            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                MovieTrailer trailer = new MovieTrailer();
                trailer.setTrailer_key(dayForecast.getString(OWM_KEY));
                trailer.setTrailer_name(dayForecast.getString(OWM_NAME));
                all_trailers.add(trailer);
            }
            movie.setTrailer_ids(all_trailers);
            return movie;
        }

        @Override
        protected Movie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {

                //http://api.themoviedb.org/3/movie/307081/videos?api_key=5ceb51e2a7d76f24c238deec492884ca

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";
                final String API_PARAM = "api_key";
                final String API_KEY = "5ceb51e2a7d76f24c238deec492884ca";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error 1", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieTrailerFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie result) {
            if (result != null) {
                setTrailerForAdapter(result);
                // New data is back from the server.  Hooray!
            }
        }
    }

    public class FetchReviewTask extends AsyncTask<String, Void, Movie> {

        private Movie getMovieReviewFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_AUTHOR = "author";
            final String OWM_CONTENT = "content";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            ArrayList<MovieReview> all_reviews = new ArrayList<>();


            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                MovieReview review = new MovieReview();
                review.setAuthor(dayForecast.getString(OWM_AUTHOR));
                review.setContent(dayForecast.getString(OWM_CONTENT));
                all_reviews.add(review);
            }
            movie.setMovie_reviews(all_reviews);
            return movie;
        }

        @Override
        protected Movie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;

            try {

                //http://api.themoviedb.org/3/movie/307081/videos?api_key=5ceb51e2a7d76f24c238deec492884ca

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?";
                final String API_PARAM = "api_key";
                final String API_KEY = "5ceb51e2a7d76f24c238deec492884ca";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).build();

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error 1", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieReviewFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if (movie != null) {
                setMovieReviewForAdapter(movie);
                // New data is back from the server.  Hooray!
            }
        }
    }

}
