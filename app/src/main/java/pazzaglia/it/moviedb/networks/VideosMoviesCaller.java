package pazzaglia.it.moviedb.networks;

import pazzaglia.it.moviedb.BuildConfig;
import pazzaglia.it.moviedb.models.MovieVideos;
import retrofit2.Call;

/**
 * Created by IO on 18/07/2016.
 */

public class VideosMoviesCaller extends AbstractApiCaller<MovieVideos>{

    private static final String TAG = "TopRatedMoviesCaller";

    @Override
    public Call<MovieVideos> specificApiCall(int movieId) {
        return getApiService()
                .getdMovieVideos(movieId, BuildConfig.MOVIEDB_APIKEY);
    }
}
