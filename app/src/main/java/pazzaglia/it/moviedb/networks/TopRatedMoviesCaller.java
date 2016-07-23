package pazzaglia.it.moviedb.networks;

import pazzaglia.it.moviedb.BuildConfig;
import pazzaglia.it.moviedb.models.Movies;
import retrofit2.Call;

/**
 * Created by IO on 18/07/2016.
 */

public class TopRatedMoviesCaller extends AbstractApiCaller<Movies>{

    private static final String TAG = "TopRatedMoviesCaller";

    @Override
    public Call<Movies> specificApiCall() {
        return getApiService()
                .getTopRatedMovies(BuildConfig.MOVIEDB_APIKEY);
    }
}
