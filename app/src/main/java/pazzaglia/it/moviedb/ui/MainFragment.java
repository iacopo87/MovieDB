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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

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

import static pazzaglia.it.moviedb.utils.Constant.SAVE_POSITION;
import static pazzaglia.it.moviedb.utils.Constant.SORTING_FAVOURITE;
import static pazzaglia.it.moviedb.utils.Constant.SORTING_POPULAR;
import static pazzaglia.it.moviedb.utils.Constant.SORTING_TOP_RATED;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {


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

    @Bind(R.id.spinner_order)
    Spinner _spinner;

    int selectedSortOrder = SORTING_POPULAR;
    private View rootView;
    private int mPosition;

    String[] MOVIE_COLUMN = {
            MovieColumns._ID,
            MovieColumns.POSTER_PATH,
            MovieColumns.ORIGINAL_TITLE,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.RELEASE_DATE,
            MovieColumns.OVERVIEW,
            MovieColumns.FAVOURITE,
            MovieColumns.POSTER_BLOB
    };

    public static int COL_MOVIE_ID = 0;
    public static int COL_POSTER_PATH = 1;
    public static int COL_ORIGINAL_TITLE = 2;
    public static int COL_VOTE_AVERAGE= 3;
    public static int COL_RELEASE_DATE = 4;
    public static int COL_OVERVIEW = 5;
    public static int COL_FAVOURITE = 6;
    public static int COL_POSTER_BLOB = 7;


    public MainFragment() {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getItemAtPosition(i).toString()){
            case "Top Rated":
                selectedSortOrder = SORTING_TOP_RATED;
                break;

            case "Most Popular":
                selectedSortOrder = SORTING_POPULAR;
                break;

            case "Favourite":
                selectedSortOrder = SORTING_FAVOURITE;
                break;
        }
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constant.PREFERENCE_SORTING, selectedSortOrder);
        editor.commit();

        if(Util.isOnline(getActivity())){
            updateMovies();
            getActivity().getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);

        selectedSortOrder = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(Constant.PREFERENCE_SORTING, SORTING_POPULAR);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_order_array,R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(adapter);
        _spinner.setOnItemSelectedListener(this);
        _spinner.setSelection(selectedSortOrder);

        gridViewCursorAdapter = new GridViewCursorAdapter(getActivity(), null, 0);
        _gridView.setAdapter(gridViewCursorAdapter);

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    mPosition = cursor.getPosition();
                    ((Callback) getActivity())
                            .onItemSelected(Parcels.wrap(createMovieToPass(cursor)));
                }
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_POSITION)){
            mPosition = savedInstanceState.getInt(SAVE_POSITION);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SAVE_POSITION, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private Movie createMovieToPass(Cursor cursor){
        Movie movie = new Movie();
        movie.setId(cursor.getInt(COL_MOVIE_ID));
        movie.setPosterPath(cursor.getString(COL_POSTER_PATH));
        movie.setOriginalTitle(cursor.getString(COL_ORIGINAL_TITLE));
        movie.setVoteAverage(cursor.getDouble(COL_VOTE_AVERAGE));
        movie.setReleaseDate(cursor.getString(COL_RELEASE_DATE));
        movie.setOverview(cursor.getString(COL_OVERVIEW));
        movie.setFavourite(cursor.getInt(COL_FAVOURITE));
        return movie;
    }

    private void updateMovies(){
        String snackBarString = "";
        selectedSortOrder = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(Constant.PREFERENCE_SORTING, SORTING_POPULAR);

        switch (selectedSortOrder){
            case SORTING_TOP_RATED:
                snackBarString = "Top Rated";
                break;

            case SORTING_POPULAR:
                snackBarString = "Most Popular";
                break;

            case SORTING_FAVOURITE:
                snackBarString = "Favourite";
                break;
        }
        loadMovies(selectedSortOrder);
        Snackbar.make(rootView, snackBarString, Snackbar.LENGTH_LONG).show();
    }

    private void loadMovies(int selectionSortOrder){
        if(selectedSortOrder != SORTING_FAVOURITE) {
            Intent intent = new Intent(getActivity(), MovieService.class);
            intent.putExtra(Constant.EXTRA_MOVIE_SORTING, selectionSortOrder);
            getActivity().startService(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = null;
        String selection = null;
        String[] selectionArgs = null;

        switch (selectedSortOrder) {
            case SORTING_POPULAR:
                sortOrder = MovieColumns.POPULARITY + " DESC";
                break;

            case SORTING_TOP_RATED:
                sortOrder = MovieColumns.VOTE_AVERAGE + " DESC";
                break;

            case SORTING_FAVOURITE:
                selection = MovieColumns.FAVOURITE +" = ?";
                selectionArgs = new String[]{String.valueOf(1)};
        }


        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                MOVIE_COLUMN,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        gridViewCursorAdapter.swapCursor(data);
        gridViewCursorAdapter.notifyDataSetChanged();
        if(mPosition != ListView.INVALID_POSITION) {
            _gridView.smoothScrollToPosition(mPosition);
        }
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
