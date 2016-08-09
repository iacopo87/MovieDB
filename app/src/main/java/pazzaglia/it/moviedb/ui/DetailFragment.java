package pazzaglia.it.moviedb.ui;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.data.MovieProvider;
import pazzaglia.it.moviedb.models.Movie;
import pazzaglia.it.moviedb.utils.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    Movie movie;


    @Bind(R.id.img_movie)
    ImageView _imgMovie;
    @Bind(R.id.txt_movie_title)
    TextView _txtMovieTitle;
    @Bind(R.id.txt_user_rating)
    TextView _txtUserRating;
    @Bind(R.id.txt_release_date)
    TextView _txtReleaseDate;
    @Bind(R.id.txt_movie_overview)
    TextView _txtMovieOverview;
    @Bind(R.id.btn_favourite)
    ToggleButton _btnFavourite;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootview);
        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = (Movie) Parcels.unwrap(arguments.getParcelable(Constant.EXTRA_MOVIE));
            //Setup the UI
            _txtMovieTitle.setText(movie.getOriginalTitle());
            _txtUserRating.setText(movie.getVoteAverage().toString());
            _txtReleaseDate.setText(movie.getReleaseDate().toString());
            _txtMovieOverview.setText(movie.getOverview());
            _btnFavourite.setChecked(movie.getFavourite() == 1);
            Picasso.with(getActivity()) //
                    .load(Constant.BASE_IMG_URL + movie.getPosterPath()) //
                    .placeholder(R.color.colorPrimary) //
                    .resize(120,180)
                    .centerCrop()
                    .tag(this) //
                    .into(_imgMovie);

        }
        _btnFavourite.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                updateToggleState(isChecked);
            }
        }) ;

        return rootview;
    }

    private void updateToggleState(boolean isChecked){
        ContentProviderOperation.Builder builder;builder = ContentProviderOperation.newUpdate(
                MovieProvider.Movies.CONTENT_URI);
        builder.withSelection(MovieColumns._ID +" = ?",  new String[]{String.valueOf(movie.getId())});
        ContentValues values = new ContentValues();
        values.put(MovieColumns.FAVOURITE, isChecked?1:0);
        getActivity().getContentResolver().update(MovieProvider.Movies.CONTENT_URI,values,MovieColumns._ID +" = ?",new String[]{String.valueOf(movie.getId())});
    }
}
