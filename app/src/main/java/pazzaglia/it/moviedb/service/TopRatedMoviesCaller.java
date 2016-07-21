package pazzaglia.it.moviedb.service;

import pazzaglia.it.moviedb.model.Movies;
import pazzaglia.it.moviedb.shared.Credentials;
import retrofit2.Call;

/**
 * Created by IO on 18/07/2016.
 */

public class TopRatedMoviesCaller extends AbstractApiCaller<Movies>{

    private static final String TAG = "TopRatedMoviesCaller";

    @Override
    public Call<Movies> specificApiCall() {
        return getApiService()
                .getTopRatedMovies(Credentials.API_KEY);
    }
}
