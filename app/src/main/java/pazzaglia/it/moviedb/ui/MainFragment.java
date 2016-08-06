package pazzaglia.it.moviedb.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.moviedb.R;
import pazzaglia.it.moviedb.adapter.GridViewCursorAdapter;
import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.data.MovieProvider;
import pazzaglia.it.moviedb.models.Movie;
import pazzaglia.it.moviedb.services.MovieService;
import pazzaglia.it.moviedb.utils.Constant;
import pazzaglia.it.moviedb.utils.Util;

import static pazzaglia.it.moviedb.utils.Constant.SORTING_POPULAR;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /* A callback interface that all activities containing this fragment must
    * implement. This mechanism allows activities to be notified of item
    * selections.
    */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Parcelable parcelableMovie);
    }

    private static final String TAG = "MainFragment";
    private static final int CURSOR_LOADER_ID = 0;

    private GridViewCursorAdapter gridViewCursorAdapter;

    @Bind(R.id.grid_view)
    GridView _gridView;
    int selectedSortOrder = SORTING_POPULAR;
    private View rootView;

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


    public MainFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.actionbar_menu, menu);
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

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Constant.PREFERENCE_SORTING, selectedSortOrder);
                editor.commit();

                if(Util.isOnline(getActivity())){
                    updateMovies();
                    getActivity().getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        gridViewCursorAdapter = new GridViewCursorAdapter(getActivity(), null, 0);
        _gridView.setAdapter(gridViewCursorAdapter);


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

                    ((Callback) getActivity())
                            .onItemSelected(Parcels.wrap(movie));

                }
            }
        });



        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateMovies(){
        selectedSortOrder = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(Constant.PREFERENCE_SORTING, SORTING_POPULAR);
        if(selectedSortOrder == Constant.SORTING_TOP_RATED){
            selectedSortOrder=Constant.SORTING_TOP_RATED;
            loadTopRatedMovies();
            Snackbar.make(rootView, "Top rated", Snackbar.LENGTH_LONG).show();
        }else {
            selectedSortOrder = SORTING_POPULAR;
            loadPopularMovies();
            Snackbar.make(rootView, "Most Popular", Snackbar.LENGTH_LONG).show();
        }
    }

    private void loadPopularMovies(){
        Intent intent = new Intent(getActivity(), MovieService.class);
        intent.putExtra(Constant.EXTRA_MOVIE_SORTING, SORTING_POPULAR);
        getActivity().startService(intent);
    }

    private void loadTopRatedMovies(){
        Intent intent = new Intent(getActivity(), MovieService.class);
        intent.putExtra(Constant.EXTRA_MOVIE_SORTING, SORTING_POPULAR);
        getActivity().startService(intent);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                MOVIE_COLUMN,
                null,
                null,
                (selectedSortOrder == SORTING_POPULAR)?MovieColumns.POPULARITY:MovieColumns.VOTE_AVERAGE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        gridViewCursorAdapter.swapCursor(data);
        gridViewCursorAdapter.notifyDataSetChanged();
        if(Util.isOnline(getActivity())){
            updateMovies();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(gridViewCursorAdapter != null) {
            gridViewCursorAdapter.swapCursor(null);
        }
    }


}
