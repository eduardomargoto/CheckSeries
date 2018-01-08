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
        public static final String COLUMN_TVDB_ID = "tvdb_id";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_TMDB_ID = "tmdb_id";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_LOGO_URL = "logo_url";
        public static final String COLUMN_BANNER_URL = "banner_url";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_FANART_URL = "fanart_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_AIR_DATE = "air_date";
        public static final String COLUMN_AIR_TIME = "air_time";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_NETWORK = "network";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_TRAILER = "trailer";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_TOTAL_EPISODES = "aired_episodes";


        public static final int POSITION_ID = 0;
        public static final int POSITION_TITLE = 1;
        public static final int POSITION_TVDB_ID = 2;
        public static final int POSITION_IMDB_ID = 3;
        public static final int POSITION_TMDB_ID = 4;
        public static final int POSITION_YEAR = 5;
        public static final int POSITION_TYPE = 6;
        public static final int POSITION_LOGO_URL = 7;
        public static final int POSITION_BANNER_URL = 8;
        public static final int POSITION_POSTER_URL = 9;
        public static final int POSITION_FANART_URL = 10;
        public static final int POSITION_OVERVIEW = 11;
        public static final int POSITION_FIRST_AIRED = 12;
        public static final int POSITION_AIR_DATE = 13;
        public static final int POSITION_AIR_TIME = 14;
        public static final int POSITION_RUNTIME = 15;
        public static final int POSITION_NETWORK = 16;
        public static final int POSITION_COUNTRY = 17;
        public static final int POSITION_UPDATED_AT = 18;
        public static final int POSITION_TRAILER = 19;
        public static final int POSITION_HOMEPAGE = 20;
        public static final int POSITION_STATUS = 21;
        public static final int POSITION_RATING = 22;
        public static final int POSITION_VOTES = 23;
        public static final int POSITION_COMMENT_COUNT = 24;
        public static final int POSITION_GENRES = 25;
        public static final int POSITION_TOTAL_EPISODES = 26;

        public static final String[] SHOWS_COLUMNS = new String[]{
                _ID
                , COLUMN_NAME
                , COLUMN_TVDB_ID
                , COLUMN_IMDB_ID
                , COLUMN_TMDB_ID
                , COLUMN_YEAR
                , COLUMN_TYPE
                , COLUMN_LOGO_URL
                , COLUMN_BANNER_URL
                , COLUMN_POSTER_URL
                , COLUMN_FANART_URL
                , COLUMN_OVERVIEW
                , COLUMN_FIRST_AIRED
                , COLUMN_AIR_DATE
                , COLUMN_AIR_TIME
                , COLUMN_RUNTIME
                , COLUMN_NETWORK
                , COLUMN_COUNTRY
                , COLUMN_UPDATED_AT
                , COLUMN_TRAILER
                , COLUMN_HOMEPAGE
                , COLUMN_STATUS
                , COLUMN_RATING
                , COLUMN_VOTES
                , COLUMN_COMMENT_COUNT
                , COLUMN_GENRES
                , COLUMN_TOTAL_EPISODES
        };

        public static Uri makeUriWithId(String tracktId) {
            return URI.buildUpon().appendPath(tracktId).build();
        }
    }
}
