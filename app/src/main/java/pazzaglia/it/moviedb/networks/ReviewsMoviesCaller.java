package pazzaglia.it.moviedb.networks;

import pazzaglia.it.moviedb.BuildConfig;
import pazzaglia.it.moviedb.models.MovieReviews;
import retrofit2.Call;

/**
 * Created by IO on 18/07/2016.
 */

public class ReviewsMoviesCaller extends AbstractApiCaller<MovieReviews>{

    private static final String TAG = "TopRatedMoviesCaller";

    @Override
    public Call<MovieReviews> specificApiCall(int movieId) {
        return getApiService()
                .getdMovieReviews(movieId, BuildConfig.MOVIEDB_APIKEY);
    }
}
