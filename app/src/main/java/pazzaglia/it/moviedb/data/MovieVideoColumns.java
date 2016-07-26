package pazzaglia.it.moviedb.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by IO on 25/07/2016.
 */

public interface MovieVideoColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String KEY = "key";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SITE = "site";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String SIZE = "size";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TYPE = "type";

    @DataType(DataType.Type.INTEGER) @References(table = MovieDatabase.MOVIE, column = MovieColumns._ID)
    public static final String MOVIE_ID = "movie_id";

}
