package com.ubpatel.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ukumar on 8/31/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private List<Movie> movies = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        this.movies = movies;
        this.context = context;
    }

    public int getCount() {
        return movies.size();
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.movie_poster_grid_view, parent, false);
            holder = new ViewHolder();
            holder.image_movie_poster = (ImageView) row.findViewById(R.id.grid_item_movie_imageview);
            holder.image_movie_poster.setAdjustViewBounds(true);
            holder.image_movie_poster.setPadding(0, 0, 0, 0);
            holder.text_movietitle = (TextView) row.findViewById(R.id.movie_title);
            holder.movie_rating = (RatingBar) row.findViewById(R.id.ratingBar);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

//        row.measure(View.MeasureSpec.makeMeasureSpec(
//                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        Picasso.with(getContext())
                .load(POSTER_BASE_URL + movies.get(position).getPoster_image_url())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.image_movie_poster);

        holder.text_movietitle.setText(movies.get(position).getOriginal_title());
        holder.movie_rating.setRating((Float.parseFloat(movies.get(position).getUser_rating()) * 5) / 10);
        return row;


        /*ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(0, 0, 0, 0);

        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(getContext()).cancelRequest(imageView);
        Picasso.with(getContext())
                .load(POSTER_BASE_URL + movies.get(position).getPoster_image_url())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(imageView);
        return imageView;*/
    }

    static class ViewHolder {
        TextView text_movietitle;
        ImageView image_movie_poster;
        RatingBar movie_rating;
    }
}
