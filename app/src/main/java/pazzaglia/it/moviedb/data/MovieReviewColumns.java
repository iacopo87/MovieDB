package pazzaglia.it.moviedb.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by IO on 25/07/2016.
 */

public interface MovieReviewColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String AUTHOR = "author";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String CONTENT = "content";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String URL = "url";

    @DataType(DataType.Type.INTEGER) @References(table = MovieDatabase.MOVIE, column = MovieColumns._ID)
    public static final String MOVIE_ID = "movie_id";

}
