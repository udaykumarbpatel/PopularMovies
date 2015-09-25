package com.ubpatel.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    final static String EXTRA_BUNDLE = "PACKAGE";
    final String LOG_TAG = "DEBUG TEST  : ";
    GridView gridView;

    public MoviePosterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movieposterfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        float scale_factor = getResources().getDisplayMetrics().density * 100;
        int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / scale_factor) / 2;
        gridView.setNumColumns(columns);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(
                getString(R.string.sort_order),
                getString(R.string.sort_order_most_popular));
        movieTask.execute(sortType);
    }

    private void setImageForAdapter(final List<Movie> result) {
        gridView.setAdapter(new MovieAdapter(getActivity(), result));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), MovieDetail.class);
                intent.putExtra(EXTRA_BUNDLE, result.get(position));
                startActivity(intent);
            }
        });
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private ArrayList<Movie> getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTE_AVERAGE = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_POSTER_PATH = "poster_path";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            ArrayList<Movie> all_movies = new ArrayList<>();

            for (int i = 0; i < weatherArray.length(); i++) {
                Movie single_movie = new Movie();
                JSONObject dayForecast = weatherArray.getJSONObject(i);
                single_movie.setOriginal_title(dayForecast.getString(OWM_ORIGINAL_TITLE));
                single_movie.setOverview(dayForecast.getString(OWM_OVERVIEW));
                single_movie.setPoster_image_url(dayForecast.getString(OWM_POSTER_PATH));
                single_movie.setRelease_date(dayForecast.getString(OWM_RELEASE_DATE));
                single_movie.setUser_rating(dayForecast.getString(OWM_VOTE_AVERAGE));
                all_movies.add(single_movie);
                //Log.d(LOG_TAG, single_movie.toString());
            }
            return all_movies;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            try {

                //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=5ceb51e2a7d76f24c238deec492884ca

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String API_PARAM = "api_key";
                final String SORT_BY_PARAM = "sort_by";
                String SORT_BY_VALUE = params[0];
                final String API_KEY = "5ceb51e2a7d76f24c238deec492884ca";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).appendQueryParameter(SORT_BY_PARAM, SORT_BY_VALUE).build();

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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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
                return getMovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                setImageForAdapter(result);
                // New data is back from the server.  Hooray!
            }
        }
    }
}
