package pazzaglia.it.moviedb.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pazzaglia.it.moviedb.data.MovieColumns;
import pazzaglia.it.moviedb.data.MovieProvider;
import pazzaglia.it.moviedb.models.Movie;
import pazzaglia.it.moviedb.models.Movies;
import pazzaglia.it.moviedb.networks.AbstractApiCaller;
import pazzaglia.it.moviedb.networks.PopularMoviesCaller;
import pazzaglia.it.moviedb.networks.TopRatedMoviesCaller;

import static pazzaglia.it.moviedb.utils.Constant.EXTRA_MOVIE_SORTING;
import static pazzaglia.it.moviedb.utils.Constant.SORTING_POPULAR;


public class MovieService extends IntentService {

    private static final String TAG = "MovieService";
    private List<Integer> idList = new ArrayList<Integer>();

    public MovieService() {
        super("MovieService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadStoredMovieId();
        int movieOrder = intent.getIntExtra(EXTRA_MOVIE_SORTING, SORTING_POPULAR);

        if(movieOrder == SORTING_POPULAR){
            loadPopularMovies();
        }else {
            loadTopRatedMovies();
        }

    }

    private void loadStoredMovieId(){
        Cursor c = getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                new String[]{MovieColumns._ID}, null, null, null);

        while (c.moveToNext()){
            idList.add(c.getInt(c.getColumnIndex(MovieColumns._ID)));
        }

    }

    private void loadPopularMovies(){
        PopularMoviesCaller popularMoviesCaller = new PopularMoviesCaller();
        popularMoviesCaller.doApiCall(getApplicationContext(), "Loading popular Movies", 0, apiCallback);
    }

    private void loadTopRatedMovies(){
        TopRatedMoviesCaller topRatedMoviesCaller = new TopRatedMoviesCaller();
        topRatedMoviesCaller.doApiCall(getApplicationContext(), "Loading top rated Movies", 0, apiCallback);
    }


    private AbstractApiCaller.MyCallbackInterface apiCallback = new AbstractApiCaller.MyCallbackInterface<Movies>() {
        @Override
        public void onDownloadFinishedOK(Movies result) {
            insertMovies(result);
        }
        @Override
        public void onDownloadFinishedKO(Movies result) {

        }
    };

    public void insertMovies(Movies movies){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        //insert only new movie
        for (Movie movie : movies.getResults()){
            ContentProviderOperation.Builder builder;
            int movieId = movie.getId();
            if(!idList.contains(movieId)) {
                builder = ContentProviderOperation.newInsert(
                        MovieProvider.Movies.CONTENT_URI);
                builder.withValue(MovieColumns._ID, movieId);
                builder.withValue(MovieColumns.POSTER_PATH, movie.getPosterPath());
                builder.withValue(MovieColumns.POPULARITY, movie.getPopularity());
                builder.withValue(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
                builder.withValue(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
                builder.withValue(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
                builder.withValue(MovieColumns.OVERVIEW, movie.getOverview());
                builder.withValue(MovieColumns.FAVOURITE, 0);
                batchOperations.add(builder.build());
            }  else {
                builder = ContentProviderOperation.newUpdate(
                        MovieProvider.Movies.CONTENT_URI);
                builder.withSelection(MovieColumns._ID +" = ?",  new String[]{String.valueOf(movieId)});
            }
        }

        try{
            getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
        } catch(RemoteException | OperationApplicationException e){
            Log.e(TAG, "Error applying batch insert", e);
        }

    }

}
