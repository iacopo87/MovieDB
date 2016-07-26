package pazzaglia.it.moviedb.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by IO on 25/07/2016.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY = "pazzaglia.it.moviedb";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String MOVIES = "movies";
        String VIDEOS = "videos";
        String REVIEWS = "reviews";
    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIE)
    public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.RELEASE_DATE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIES_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.MOVIE_REVIEW)
    public static class MovieReview {

        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/review",
                defaultSort = MovieReviewColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "MOVIES_REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = MovieReviewColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.REVIEWS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.MOVIE_VIDEO)
    public static class MovieVideo {

        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/video",
                defaultSort = MovieVideoColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "MOVIES_VIDEO_ID",
                path = Path.VIDEOS + "/#",
                type = "vnd.android.cursor.item/video",
                whereColumn = MovieVideoColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.VIDEOS, String.valueOf(id));
        }
    }

}