package pazzaglia.it.moviedb.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
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
            byte[] imgByte = movie.getImageBlob();
            _imgMovie.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
            /*Picasso.with(getActivity()) //
                    .load(Constant.BASE_IMG_URL + movie.getPosterPath()) //
                    .placeholder(R.color.colorPrimary) //
                    .resize(120,180)
                    .centerCrop()
                    .tag(this) //
                    .into(_imgMovie);*/
        }


        return rootview;
    }
}
