package pazzaglia.it.moviedb.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.utils.Constant;

/**
 * Created by IO on 12/07/2016.
 */


public class GridViewCursorAdapter extends CursorAdapter{

    static class ViewHolder
    {
        @Bind(R.id.image_movie)
        ImageView imageView;
        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    private Context context;

    public GridViewCursorAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        this.context = context;
    }

    private void loadPosterImage(ViewHolder holder, Cursor c){
        // Get the image URL for the current position
        byte[] imgByte = c.getBlob(c.getColumnIndex(MovieColumns.POSTER_BLOB));
        if(imgByte != null && imgByte.length != 0) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        } else {
            String posterPathUrl = c.getString(c.getColumnIndex(MovieColumns.POSTER_PATH));
            Picasso.with(context) //
                    .load(Constant.BASE_IMG_URL + posterPathUrl) //
                    .placeholder(R.color.colorPrimaryDark) //
                    .fit() //
                    .tag(context) //
                    .into(holder.imageView);
        }
        // Trigger the download of the URL asynchronously into the image view
       /* Picasso.with(context) //
                .load(Constant.BASE_IMG_URL + posterPathUrl) //
                .placeholder(R.color.colorPrimaryDark) //
                .fit() //
                .tag(context) //
                .into(holder.imageView);*/
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.grid_item, parent, false);
        ViewHolder holder = new ViewHolder(convertView);
        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        loadPosterImage(holder, cursor);
    }



}

