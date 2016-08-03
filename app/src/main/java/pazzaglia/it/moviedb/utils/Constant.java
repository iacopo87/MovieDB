package pazzaglia.it.moviedb.utils;

/**
 * Created by IO on 13/07/2016.
 */

public class Constant {
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static  final String BASE_IMG_URL = "http://image.tmdb.org/t/p/w185";

    //Sorting Order
    public static final int SORTING_POPULAR = 0;
    public static final int SORTING_TOP_RATED = 1;

    //Preference
    public static final String PREFERENCE_SORTING = "PREFERENCE_SORTING";

    //Intent extra
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String EXTRA_MOVIE_SORTING = "EXTRA_MOVIE_SORTING";
}
