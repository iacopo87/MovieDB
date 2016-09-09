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
import pazzaglia.it.moviedb.models.MovieVideo;

/**
 * Created by IO on 09/09/2016.
 */

public class TrailerArrayAdapter extends ArrayAdapter<MovieVideo> {

    static class ViewHolder
    {
        @Bind(R.id.txt_trailer_name)
        TextView txtTrailerName;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
    private static final String LOG_TAG = TrailerArrayAdapter.class.getSimpleName();

    public TrailerArrayAdapter(Context context, List<MovieVideo> movieVideos) {
        super(context, 0, movieVideos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MovieVideo movieVideo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_trailer_item, parent, false);
            TrailerArrayAdapter.ViewHolder holder = new TrailerArrayAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }
        TrailerArrayAdapter.ViewHolder holder = (TrailerArrayAdapter.ViewHolder)convertView.getTag();

        holder.txtTrailerName.setText(movieVideo.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(movieVideo.getKey());
            }
        });
        return convertView;
    }


    private void watchYoutubeVideo(String id){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        getContext().startActivity(intent);
    }
}