package pazzaglia.it.moviedb.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.adapter.GridViewAdapter;
import pazzaglia.it.moviedb.model.Movie;
import pazzaglia.it.moviedb.model.Movies;
import pazzaglia.it.moviedb.service.AbstractApiCaller;
import pazzaglia.it.moviedb.service.PopularMoviesCaller;
import pazzaglia.it.moviedb.service.TopRatedMoviesCaller;
import pazzaglia.it.moviedb.shared.Constant;

public class GridMovieActivity extends AppCompatActivity {
    private GridViewAdapter gridViewAdapter;
    @Bind(R.id.grid_view)
    GridView _gridView;
    int selectedSortOrder = Constant.SORTING_POPULAR;

    private AbstractApiCaller.MyCallbackInterface apiCallback = new AbstractApiCaller.MyCallbackInterface<Movies>() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_movie);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        updateMovies();

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Intent intent = new Intent(GridMovieActivity.this, MovieDetailActivity.class);
                intent.putExtra(Constant.EXTRA_MOVIE, Parcels.wrap(movie));
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_toggle_sort:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                selectedSortOrder = (selectedSortOrder == Constant.SORTING_TOP_RATED)?
                        Constant.SORTING_POPULAR:
                        Constant.SORTING_TOP_RATED;

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Constant.PREFERENCE_SORTING, selectedSortOrder);

                editor.commit();
                updateMovies();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadPopularMovies(){
        PopularMoviesCaller popularMoviesCaller = new PopularMoviesCaller();
        popularMoviesCaller.doApiCall(this, "Loading popular Movies",apiCallback );
    }

    private void loadTopRatedMovies(){
        TopRatedMoviesCaller topRatedMoviesCaller = new TopRatedMoviesCaller();
        topRatedMoviesCaller.doApiCall(this, "Loading top rated Movies", apiCallback);
    }

    private void updateMovies(){
        selectedSortOrder = getPreferences(Context.MODE_PRIVATE).getInt(Constant.PREFERENCE_SORTING, Constant.SORTING_POPULAR);
        if(selectedSortOrder == Constant.SORTING_TOP_RATED){
            selectedSortOrder=Constant.SORTING_TOP_RATED;
            loadTopRatedMovies();
            Snackbar.make(findViewById(android.R.id.content), "Top rated", Snackbar.LENGTH_LONG).show();
        }else {
            selectedSortOrder = Constant.SORTING_POPULAR;
            loadPopularMovies();
            Snackbar.make(findViewById(android.R.id.content), "Most Popular", Snackbar.LENGTH_LONG).show();
        }
    }
}