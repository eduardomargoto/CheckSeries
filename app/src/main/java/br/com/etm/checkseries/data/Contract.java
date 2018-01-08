package br.com.etm.checkseries.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import br.com.etm.checkseries.BuildConfig;

/**
 * Created by eduardo on 07/01/18.
 */

public class Contract {

    static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    static final String PATH_SHOW = "show";
    static final String PATH_SHOW_BY_ID = "show/*";

    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public Contract() {
    }

    public static final class Show implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_SHOW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_SHOW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_SHOW;

        public static final String TABLE_NAME = "shows";

        public static final String COLUMN_NAME = "name";

        public static final int POSITION_ID = 0;
        public static final int POSITION_NAME = 1;

        public static final String[] SHOWS_COLUMNS = new String[]{
                _ID,
                COLUMN_NAME
        };

        public static Uri makeUriWithId(String tracktId) {
            return URI.buildUpon().appendPath(tracktId).build();
        }
    }
}
