package pazzaglia.it.moviedb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.model.Movie;
import pazzaglia.it.moviedb.shared.Constant;

public class MovieDetailActivity extends AppCompatActivity {

    Movie movie;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get movie from intent
        Intent intent = getIntent();
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Constant.EXTRA_MOVIE));

        //Setup the UI
        _txtMovieTitle.setText(movie.getOriginalTitle());
        _txtUserRating.setText(movie.getVoteAverage().toString());
        _txtReleaseDate.setText(movie.getReleaseDate().toString());
        _txtMovieOverview.setText(movie.getOverview());
        Picasso.with(this) //
                .load(Constant.BASE_IMG_URL + movie.getPosterPath()) //
                .placeholder(R.color.colorPrimary) //
                .resize(120,180)
                .centerCrop()
                .tag(this) //
                .into(_imgMovie);
    }

}
