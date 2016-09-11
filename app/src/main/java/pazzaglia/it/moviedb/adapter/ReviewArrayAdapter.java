package pazzaglia.it.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.models.MovieReview;

/**
 * Created by IO on 09/09/2016.
 */

public class ReviewArrayAdapter extends ArrayAdapter<MovieReview> {

    static class ViewHolder
    {
        @Bind(R.id.txt_review_author)
        TextView txtReviewAuthor;

        @Bind(R.id.txt_review_body)
        TextView txtReviewBody;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
    private static final String LOG_TAG = ReviewArrayAdapter.class.getSimpleName();

    public ReviewArrayAdapter(Context context, List<MovieReview> movieReviews) {
        super(context, 0, movieReviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MovieReview movieReviews = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_review_item, parent, false);
            ReviewArrayAdapter.ViewHolder holder = new ReviewArrayAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ReviewArrayAdapter.ViewHolder holder = (ReviewArrayAdapter.ViewHolder)convertView.getTag();

        holder.txtReviewAuthor.setText(movieReviews.getAuthor());
        holder.txtReviewBody.setText(movieReviews.getContent());

        holder.txtReviewBody.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(movieReviews.getUrl()));
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }

}