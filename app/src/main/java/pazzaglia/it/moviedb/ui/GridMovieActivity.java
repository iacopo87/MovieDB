package pazzaglia.it.moviedb.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.service.PopularMoviesCaller;

public class GridMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_movie);
        new PopularMoviesCaller().doApiCall(this, "Loading popular movies");
    }
}
