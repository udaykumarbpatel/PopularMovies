package com.ubpatel.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ukumar on 10/12/2015.
 */
public class ReviewAdapter extends ArrayAdapter<MovieReview> {
    private List<MovieReview> movie_review = new ArrayList<>();
    private Context context;

    public ReviewAdapter(Context context, List<MovieReview> movies) {
        super(context, 0, movies);
        this.movie_review = movies;
        this.context = context;
    }

    public int getCount() {
        return movie_review.size();
    }

    public MovieReview getItem(int position) {
        return movie_review.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view_reviews, parent, false);
            holder = new ViewHolder();
            holder.review_author = (TextView) row.findViewById(R.id.author);
            holder.review_content = (TextView) row.findViewById(R.id.content);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.review_author.setText(movie_review.get(position).getAuthor());
        holder.review_content.setText(movie_review.get(position).getContent());
        return row;
    }

    static class ViewHolder {
        TextView review_author;
        TextView review_content;
    }
}
