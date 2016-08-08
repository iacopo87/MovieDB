package pazzaglia.it.moviedb.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by IO on 25/07/2016.
 */

public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @IfNotExists
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String ORIGINAL_TITLE = "original_title";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String OVERVIEW = "overview";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String FAVOURITE = "favourite";

    @DataType(DataType.Type.BLOB)
    public static final String POSTER_BLOB = "poster_blob";

}
