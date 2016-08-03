package pazzaglia.it.moviedb.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import pazzaglia.it.moviedb.adapter.GridViewCursorAdapter;
import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.data.MovieProvider;
import pazzaglia.it.moviedb.models.Movie;
import pazzaglia.it.moviedb.services.MovieService;
import pazzaglia.it.moviedb.utils.Constant;
import pazzaglia.it.moviedb.utils.Util;

import static pazzaglia.it.moviedb.utils.Constant.SORTING_POPULAR;

public class GridMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "GridMovieActivity";
    private static final int CURSOR_LOADER_ID = 0;

    private GridViewAdapter gridViewAdapter;
    private GridViewCursorAdapter gridViewCursorAdapter;

    @Bind(R.id.grid_view)
    GridView _gridView;
    int selectedSortOrder = SORTING_POPULAR;

    String[] MOVIE_COLUMN = {
            MovieColumns._ID,
            MovieColumns.POSTER_PATH,
            MovieColumns.ORIGINAL_TITLE,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.RELEASE_DATE,
            MovieColumns.OVERVIEW,
            MovieColumns.FAVOURITE
    };

    public static int COL_MOVIE_ID = 0;
    public static int COL_POSTER_PATH = 1;
    public static int COL_ORIGINAL_TITLE = 2;
    public static int COL_VOTE_AVERAGE= 3;
    public static int COL_RELEASE_DATE = 4;
    public static int COL_OVERVIEW = 5;
    public static int COL_FAVOURITE = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_movie);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        gridViewCursorAdapter = new GridViewCursorAdapter(this, null, 0);
        _gridView.setAdapter(gridViewCursorAdapter);
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    Movie movie = new Movie();
                    movie.setId(cursor.getInt(COL_MOVIE_ID));
                    movie.setPosterPath(cursor.getString(COL_POSTER_PATH));
                    movie.setOriginalTitle(cursor.getString(COL_ORIGINAL_TITLE));
                    movie.setVoteAverage(cursor.getDouble(COL_VOTE_AVERAGE));
                    movie.setReleaseDate(cursor.getString(COL_RELEASE_DATE));
                    movie.setOverview(cursor.getString(COL_OVERVIEW));
                    movie.setFavourite(cursor.getInt(COL_FAVOURITE));
                    Intent intent = new Intent(GridMovieActivity.this, MovieDetailActivity.class);
                    intent.putExtra(Constant.EXTRA_MOVIE, Parcels.wrap(movie));
                    startActivity(intent);

                }
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
                        SORTING_POPULAR:
                        Constant.SORTING_TOP_RATED;

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Constant.PREFERENCE_SORTING, selectedSortOrder);
                editor.commit();

                if(Util.isOnline(this)){
                    updateMovies();
                    getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateMovies(){
        selectedSortOrder = getPreferences(Context.MODE_PRIVATE).getInt(Constant.PREFERENCE_SORTING, SORTING_POPULAR);
        if(selectedSortOrder == Constant.SORTING_TOP_RATED){
            selectedSortOrder=Constant.SORTING_TOP_RATED;
            loadTopRatedMovies();
            Snackbar.make(findViewById(android.R.id.content), "Top rated", Snackbar.LENGTH_LONG).show();
        }else {
            selectedSortOrder = SORTING_POPULAR;
            loadPopularMovies();
            Snackbar.make(findViewById(android.R.id.content), "Most Popular", Snackbar.LENGTH_LONG).show();
        }
    }

    private void loadPopularMovies(){
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra(Constant.EXTRA_MOVIE_SORTING, SORTING_POPULAR);
        startService(intent);
    }

    private void loadTopRatedMovies(){
        Intent intent = new Intent(this, MovieService.class);
        intent.putExtra(Constant.EXTRA_MOVIE_SORTING, SORTING_POPULAR);
        startService(intent);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, MovieProvider.Movies.CONTENT_URI,
                MOVIE_COLUMN,
                null,
                null,
                (selectedSortOrder == SORTING_POPULAR)?MovieColumns.POPULARITY:MovieColumns.VOTE_AVERAGE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        gridViewCursorAdapter.swapCursor(data);
        gridViewCursorAdapter.notifyDataSetChanged();
        updateMovies();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(gridViewCursorAdapter != null) {
            gridViewCursorAdapter.swapCursor(null);
        }
    }

}

