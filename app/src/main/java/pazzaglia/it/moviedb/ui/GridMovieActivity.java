package pazzaglia.it.moviedb.ui;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.adapter.GridViewAdapter;
import pazzaglia.it.moviedb.adapter.GridViewCursorAdapter;
import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.data.MovieProvider;
import pazzaglia.it.moviedb.models.Movie;
import pazzaglia.it.moviedb.models.Movies;
import pazzaglia.it.moviedb.networks.AbstractApiCaller;
import pazzaglia.it.moviedb.networks.PopularMoviesCaller;
import pazzaglia.it.moviedb.networks.TopRatedMoviesCaller;
import pazzaglia.it.moviedb.utils.Constant;

public class GridMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "GridMovieActivity";
    private static final int CURSOR_LOADER_ID = 0;

    private GridViewAdapter gridViewAdapter;
    private GridViewCursorAdapter gridViewCursorAdapter;

    @Bind(R.id.grid_view)
    GridView _gridView;
    int selectedSortOrder = Constant.SORTING_POPULAR;

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


    private AbstractApiCaller.MyCallbackInterface apiCallback = new AbstractApiCaller.MyCallbackInterface<Movies>() {
        @Override
        public void onDownloadFinishedOK(Movies result) {
            //gridViewAdapter = new GridViewAdapter(GridMovieActivity.this,
                    //result.getResults());
            //_gridView.setAdapter(gridViewAdapter);
           // gridViewAdapter.notifyDataSetChanged();
            insertMovies(result);
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

/*        Cursor c = getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                null, null, null, null);

        if (c == null || c.getCount() == 0 || !c.moveToFirst()){
            updateMovies();
            gridViewCursorAdapter = new GridViewCursorAdapter(this, null, 0);
        } else {
            gridViewCursorAdapter = new GridViewCursorAdapter(this, c, 0);
        }*/
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
        popularMoviesCaller.doApiCall(this, "Loading popular Movies", 0, apiCallback);
    }

    private void loadTopRatedMovies(){
        TopRatedMoviesCaller topRatedMoviesCaller = new TopRatedMoviesCaller();
        topRatedMoviesCaller.doApiCall(this, "Loading top rated Movies", 0, apiCallback);
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
    public void insertMovies(Movies movies){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        for (Movie movie : movies.getResults()){
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    MovieProvider.Movies.CONTENT_URI);
            builder.withValue(MovieColumns._ID, movie.getId());
            builder.withValue(MovieColumns.POSTER_PATH, movie.getPosterPath());
            builder.withValue(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
            builder.withValue(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
            builder.withValue(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
            builder.withValue(MovieColumns.OVERVIEW, movie.getOverview());
            builder.withValue(MovieColumns.FAVOURITE, 0);
            batchOperations.add(builder.build());
        }

        try{
           getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch(RemoteException | OperationApplicationException e){
            Log.e(TAG, "Error applying batch insert", e);
        }

    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, MovieProvider.Movies.CONTENT_URI,
                MOVIE_COLUMN,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (gridViewCursorAdapter == null) {
            gridViewCursorAdapter = new GridViewCursorAdapter(this, data, 0);
        } else {
            gridViewCursorAdapter.swapCursor(data);
        }
        gridViewCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(gridViewCursorAdapter != null) {
            gridViewCursorAdapter.swapCursor(null);
        }
    }

}

