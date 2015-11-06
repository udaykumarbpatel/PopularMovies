package com.ubpatel.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ubpatel.popularmovies.data.DataManager;

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

public class MovieDetailFragment extends Fragment {

    private static DataManager dm;
    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
    final String LOG_TAG = "Log Msg : ";
    Movie movie;
    ListView trailer_list;
    ListView review_list;
    ImageView favorite_icon;
    boolean fav = false;
    View rootView;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        dm = new DataManager(getActivity());

        Bundle data = getActivity().getIntent().getExtras();

        if (data == null) {
            Toast toast = Toast.makeText(getContext(), "No Data Received", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            movie = data.getParcelable(MoviePosterFragment.EXTRA_BUNDLE);
            String isDatabase = data.getString(MoviePosterFragment.EXTRA_DATABASE);

            TextView movie_title = (TextView) rootView.findViewById(R.id.original_title);
            TextView synopsis = (TextView) rootView.findViewById(R.id.overview);
            RatingBar vote_average = (RatingBar) rootView.findViewById(R.id.ratingBar);
            TextView release_date = (TextView) rootView.findViewById(R.id.release_date);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView genre_list = (TextView) rootView.findViewById(R.id.genre);
            trailer_list = (ListView) rootView.findViewById(R.id.listView);
            review_list = (ListView) rootView.findViewById(R.id.listView_review);
            favorite_icon = (ImageView) rootView.findViewById(R.id.favorite_icon);
            favorite_icon.setImageResource(R.drawable.unfavorite);

            if (dm.getFavorite(movie.getMovie_id())) {
                fav = true;
                favorite_icon.setImageResource(R.drawable.favorite);
            }

            favorite_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fav) {
                        dm.deleteMovie(movie);
                        Toast toast = Toast.makeText(getContext(), "Deleted from Favorite", Toast.LENGTH_SHORT);
                        toast.show();
                        fav = false;
                        favorite_icon.setImageResource(R.drawable.unfavorite);
                    } else {
                        dm.insertMovie(movie);
                        Toast toast = Toast.makeText(getContext(), "Added to Favorite", Toast.LENGTH_SHORT);
                        toast.show();
                        fav = true;
                        favorite_icon.setImageResource(R.drawable.favorite);
                    }
                }
            });

            imageView.setAdjustViewBounds(true);
            Picasso.with(getContext())
                    .load(POSTER_BASE_URL + movie.getPoster_image_url())
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .into(imageView);

            if (movie.getMovie_genre().length() > 1) {
                String[] parts = movie.getMovie_genre().split("\\|");
                String genre = "";
                for (int i = 0; i < parts.length; i++) {
                    genre = genre + genreInttoString(Integer.parseInt(parts[i]));
                }
                genre_list.setText(genre);
            } else {
                genre_list.setText("Genre not available");
            }


            movie_title.setText(movie.getOriginal_title());
            if (movie.getOverview().equals("null")) {
                synopsis.setText("Overview Not Available");
            } else {
                synopsis.setText(movie.getOverview());
            }
            vote_average.setRating((Float.parseFloat(movie.getUser_rating()) * 5) / 10);
            if (movie.getRelease_date().equals("null")) {
                release_date.setText("Not Available");
            } else {
                release_date.setText(movie.getRelease_date());
            }


            if (isDatabase.equals("TRUE")) {
                movie.setTrailer_ids(dm.getAllTrailers(movie));
                setTrailerForAdapter(movie);
            } else {
                FetchTrailerTask movieTask = new FetchTrailerTask();
                movieTask.execute(movie.getMovie_id());
            }

            if (isDatabase.equals("TRUE")) {
                movie.setMovie_reviews(dm.getAllReviews(movie));
                setMovieReviewForAdapter(movie);
            } else {
                FetchReviewTask reviewTask = new FetchReviewTask();
                reviewTask.execute(movie.getMovie_id());
            }
        }
        return rootView;
    }

    private String genreInttoString(int genre_id) {
        switch (genre_id) {
            case 28:
                return "Action | ";
            case 12:
                return "Adventure | ";
            case 16:
                return "Animation | ";
            case 35:
                return "Comedy | ";
            case 80:
                return "Crime | ";
            case 99:
                return "Documentary | ";
            case 18:
                return "Drama | ";
            case 10751:
                return "Family | ";
            case 14:
                return "Fantasy | ";
            case 10769:
                return "Foreign | ";
            case 36:
                return "History | ";
            case 27:
                return "Horror | ";
            case 10402:
                return "Music | ";
            case 9648:
                return "Mystery | ";
            case 10749:
                return "Romance | ";
            case 878:
                return "Science Fiction | ";
            case 10770:
                return "TV movie | ";
            case 53:
                return "Thriller | ";
            case 10752:
                return "War | ";
            case 37:
                return "Western | ";
            default:
                return "";
        }
    }

    private void setTrailerForAdapter(final Movie result) {
        if (movie.getTrailer_ids().size() == 0) {
            View hr_line = rootView.findViewById(R.id.horizontal_line1);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) hr_line.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.empty_list_view);
        }
        trailer_list.setEmptyView(rootView.findViewById(R.id.empty_list_view));
        trailer_list.setAdapter(new TrailerAdapter(getContext(), result.getTrailer_ids()));
        trailer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + result.getTrailer_ids().get(position).getTrailer_key()));
                startActivity(intent);
            }
        });

    }

    private void setMovieReviewForAdapter(final Movie result) {
        review_list.setEmptyView(rootView.findViewById(R.id.empty_list_view_review));
        review_list.setAdapter(new ReviewAdapter(getContext(), result.getMovie_reviews()));
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

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?";
                final String API_PARAM = "api_key";
                final String API_KEY = "5ceb51e2a7d76f24c238deec492884ca";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
