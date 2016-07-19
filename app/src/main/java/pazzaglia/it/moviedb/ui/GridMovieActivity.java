package pazzaglia.it.moviedb.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.adapter.GridViewAdapter;
import pazzaglia.it.moviedb.model.Movies;
import pazzaglia.it.moviedb.service.AbstractApiCaller;
import pazzaglia.it.moviedb.service.PopularMoviesCaller;

public class GridMovieActivity extends AppCompatActivity {
    private GridViewAdapter gridViewAdapter;

    @Bind(R.id.grid_view)
    GridView _gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_movie);
        ButterKnife.bind(this);

        PopularMoviesCaller popularMoviesCaller  = new PopularMoviesCaller();
        popularMoviesCaller.doApiCall(this, "loading popular Movies", new AbstractApiCaller.MyCallbackInterface<Movies>() {
            @Override
            public void onDownloadFinishedOK(Movies result) {
                gridViewAdapter = new GridViewAdapter(GridMovieActivity.this,
                                                        result.getResults());
                _gridView.setAdapter(gridViewAdapter);
                gridViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDownloadFinishedKO(Movies result) {

            }
        });
    }
}
