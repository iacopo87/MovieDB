package pazzaglia.it.moviedb.service;

import pazzaglia.it.moviedb.model.Movies;
import pazzaglia.it.moviedb.shared.Credentials;
import retrofit2.Call;

/**
 * Created by IO on 18/07/2016.
 */

public class PopularMoviesCaller extends AbstractApiCaller<Movies>{

    private static final String TAG = "PopularMoviesCaller";

    @Override
    public Call<Movies> specificApiCall() {
        return getApiService()
                .getPopularMovies(Credentials.API_KEY);
    }
}
