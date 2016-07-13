package pazzaglia.it.moviedb.service;

import pazzaglia.it.moviedb.model.Movie;
import pazzaglia.it.moviedb.model.Movies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by IO on 13/07/2016.
 */

public interface TheMovieDBInterface {

    @GET("movie/popular")
    Call<Movies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getdMovieDetail(@Path("id") int movieId, @Query("api_key") String apiKey);

}
