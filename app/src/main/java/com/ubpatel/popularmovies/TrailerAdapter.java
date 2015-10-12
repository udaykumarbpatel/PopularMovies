package com.ubpatel.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ukumar on 10/11/2015.
 */
public class TrailerAdapter extends ArrayAdapter<MovieTrailer> {
    final String POSTER_BASE_URL = "http://img.youtube.com/vi/";
    private List<MovieTrailer> movie_trailer = new ArrayList<>();
    private Context context;

    public TrailerAdapter(Context context, List<MovieTrailer> movies) {
        super(context, 0, movies);
        this.movie_trailer = movies;
        this.context = context;
    }

    public int getCount() {
        return movie_trailer.size();
    }

    public MovieTrailer getItem(int position) {
        return movie_trailer.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view_trailer, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.clip_image);
            holder.titleTextView = (TextView) row.findViewById(R.id.trailer_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        row.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        Picasso.with(getContext())
                .load(POSTER_BASE_URL + movie_trailer.get(position).getTrailer_key() + "/default.jpg")
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(holder.imageView);

        holder.titleTextView.setText(movie_trailer.get(position).getTrailer_name());

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }

}
