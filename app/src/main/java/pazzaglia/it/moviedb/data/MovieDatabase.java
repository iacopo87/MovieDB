package pazzaglia.it.moviedb.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by IO on 25/07/2016.
 */


@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {

    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String MOVIE = "movie";
}

