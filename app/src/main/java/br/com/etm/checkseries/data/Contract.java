package br.com.etm.checkseries.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.ObjectArrays;

import br.com.etm.checkseries.BuildConfig;
import br.com.etm.checkseries.data.preferences.Preferences;

public class Contract {

    static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    static final String PATH_SHOW = "show";
    static final String PATH_SHOW_BY_ID = "show/*";

    static final String PATH_EPISODE = "episode";
    static final String PATH_EPISODE_WITH_SHOW = "episode_show";
    static final String PATH_EPISODE_BY_ID = "episode/*";

    static final String PATH_NEXTEPISODE = "nextepisode";

    static final String PATH_SEASON = "season";

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
        public static final String COLUMN_BACKGROUND_URL = "background_url";
        public static final String COLUMN_BANNER_URL = "banner_url";
        public static final String COLUMN_POSTER_URL = "poster_url";
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
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_HIDDEN = "hidden";
        public static final String COLUMN_UNFINISHED = "not_finished";

        public static final int POSITION_ID = 0;
        public static final int POSITION_TITLE = 1;
        public static final int POSITION_TVDB_ID = 2;
        public static final int POSITION_IMDB_ID = 3;
        public static final int POSITION_TMDB_ID = 4;
        public static final int POSITION_YEAR = 5;
        public static final int POSITION_TYPE = 6;
        public static final int POSITION_BACKGROUND_URL = 7;
        public static final int POSITION_BANNER_URL = 8;
        public static final int POSITION_POSTER_URL = 9;
        public static final int POSITION_OVERVIEW = 10;
        public static final int POSITION_FIRST_AIRED = 11;
        public static final int POSITION_AIR_DATE = 12;
        public static final int POSITION_AIR_TIME = 13;
        public static final int POSITION_RUNTIME = 14;
        public static final int POSITION_NETWORK = 15;
        public static final int POSITION_COUNTRY = 16;
        public static final int POSITION_UPDATED_AT = 17;
        public static final int POSITION_TRAILER = 18;
        public static final int POSITION_HOMEPAGE = 19;
        public static final int POSITION_STATUS = 20;
        public static final int POSITION_RATING = 21;
        public static final int POSITION_VOTES = 22;
        public static final int POSITION_COMMENT_COUNT = 23;
        public static final int POSITION_GENRES = 24;
        public static final int POSITION_TOTAL_EPISODES = 25;
        public static final int POSITION_FAVOURITE = 26;
        public static final int POSITION_HIDDEN = 27;
        public static final int POSITION_UNFINISHED = 28;

        public static final String[] SHOWS_COLUMNS = new String[]{
                TABLE_NAME + "." + _ID
                , TABLE_NAME + "." + COLUMN_NAME
                , TABLE_NAME + "." + COLUMN_TVDB_ID
                , TABLE_NAME + "." + COLUMN_IMDB_ID
                , TABLE_NAME + "." + COLUMN_TMDB_ID
                , TABLE_NAME + "." + COLUMN_YEAR
                , TABLE_NAME + "." + COLUMN_TYPE
                , TABLE_NAME + "." + COLUMN_BACKGROUND_URL
                , TABLE_NAME + "." + COLUMN_BANNER_URL
                , TABLE_NAME + "." + COLUMN_POSTER_URL
                , TABLE_NAME + "." + COLUMN_OVERVIEW
                , TABLE_NAME + "." + COLUMN_FIRST_AIRED
                , TABLE_NAME + "." + COLUMN_AIR_DATE
                , TABLE_NAME + "." + COLUMN_AIR_TIME
                , TABLE_NAME + "." + COLUMN_RUNTIME
                , TABLE_NAME + "." + COLUMN_NETWORK
                , TABLE_NAME + "." + COLUMN_COUNTRY
                , TABLE_NAME + "." + COLUMN_UPDATED_AT
                , TABLE_NAME + "." + COLUMN_TRAILER
                , TABLE_NAME + "." + COLUMN_HOMEPAGE
                , TABLE_NAME + "." + COLUMN_STATUS
                , TABLE_NAME + "." + COLUMN_RATING
                , TABLE_NAME + "." + COLUMN_VOTES
                , TABLE_NAME + "." + COLUMN_COMMENT_COUNT
                , TABLE_NAME + "." + COLUMN_GENRES
                , TABLE_NAME + "." + COLUMN_TOTAL_EPISODES
                , TABLE_NAME + "." + COLUMN_FAVOURITE
                , TABLE_NAME + "." + COLUMN_HIDDEN
                , TABLE_NAME + "." + COLUMN_UNFINISHED
        };

        public static Uri makeUriWithId(String tracktId) {
            return URI.buildUpon().appendPath(tracktId).build();
        }

        public static String makeWhereFilter(Preferences preferences) {
            String where = "";
            boolean favourite = preferences.isFilterFavourite();
            boolean hidden = preferences.isFilterHidden();
            boolean notFinished = preferences.isFilterUnfinished();

            if (favourite) {
                where = where + COLUMN_FAVOURITE + " = " + (favourite ? 1 : 0);
            }

            if (hidden) {
                if (!where.isEmpty()) where = where + " AND ";
                where = where + COLUMN_HIDDEN + " = " + (hidden ? 1 : 0);
            }

            if (notFinished) {
                if (!where.isEmpty()) where = where + " AND ";
                where = where + COLUMN_UNFINISHED + " = " + (notFinished ? 1 : 0);
            }

            return where;
        }

        public static String makeOrder(Preferences preferences) {
            if (preferences.isOrderName()) {
                return COLUMN_NAME + " ASC ";
            } else if (preferences.isOrderNextEpisode()) {
                return COLUMN_AIR_DATE + " DESC ";
            }
            return null;
        }
    }

    public static final class Episode implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_EPISODE).build();
        public static final Uri URI_WITH_SHOW = BASE_URI.buildUpon().appendPath(PATH_EPISODE_WITH_SHOW).build();
        public static final Uri NEXTEPISODE_URI = BASE_URI.buildUpon().appendPath(PATH_NEXTEPISODE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_EPISODE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_EPISODE;

        public static final String TABLE_NAME = "episodes";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TVDB_ID = "tvdb_id";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_TMDB_ID = "tmdb_id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_NUMBER_ABS = "number_abs";
        public static final String COLUMN_SEASON = "season";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_AVAILABLE_TRANSLATIONS = "available_translations";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_BACKGROUND_URL = "background_url";
        public static final String COLUMN_WATCHED = "watched";
        public static final String COLUMN_SEASON_ID = "season_trakt_id";

        public static final int POSITION_ID = 0;
        public static final int POSITION_TITLE = 1;
        public static final int POSITION_TVDB_ID = 2;
        public static final int POSITION_IMDB_ID = 3;
        public static final int POSITION_TMDB_ID = 4;
        public static final int POSITION_NUMBER = 5;
        public static final int POSITION_NUMBER_ABS = 6;
        public static final int POSITION_SEASON = 7;
        public static final int POSITION_FIRST_AIRED = 8;
        public static final int POSITION_UPDATED_AT = 9;
        public static final int POSITION_RATING = 10;
        public static final int POSITION_VOTES = 11;
        public static final int POSITION_COMMENT_COUNT = 12;
        public static final int POSITION_AVAILABLE_TRANSLATIONS = 13;
        public static final int POSITION_RUNTIME = 14;
        public static final int POSITION_OVERVIEW = 15;
        public static final int POSITION_BACKGROUND_URL = 16;
        public static final int POSITION_SEASON_ID = 17;

        public static final String[] COLUMNS = new String[]{
                TABLE_NAME + "." + _ID
                , TABLE_NAME + "." + COLUMN_TITLE
                , TABLE_NAME + "." + COLUMN_TVDB_ID
                , TABLE_NAME + "." + COLUMN_IMDB_ID
                , TABLE_NAME + "." + COLUMN_TMDB_ID
                , TABLE_NAME + "." + COLUMN_NUMBER
                , TABLE_NAME + "." + COLUMN_NUMBER_ABS
                , TABLE_NAME + "." + COLUMN_SEASON
                , TABLE_NAME + "." + COLUMN_OVERVIEW
                , TABLE_NAME + "." + COLUMN_FIRST_AIRED
                , TABLE_NAME + "." + COLUMN_UPDATED_AT
                , TABLE_NAME + "." + COLUMN_RATING
                , TABLE_NAME + "." + COLUMN_VOTES
                , TABLE_NAME + "." + COLUMN_COMMENT_COUNT
                , TABLE_NAME + "." + COLUMN_AVAILABLE_TRANSLATIONS
                , TABLE_NAME + "." + COLUMN_RUNTIME
                , TABLE_NAME + "." + COLUMN_BACKGROUND_URL
                , TABLE_NAME + "." + COLUMN_WATCHED
                , TABLE_NAME + "." + COLUMN_SEASON_ID
        };



        public static String[] COLUMNS_NEXTEPISODE = ObjectArrays.concat(COLUMNS, Season.COLUMNS, String.class);

        public static Uri makeUriWithId(Integer tracktId) {
            return URI.buildUpon().appendPath(String.valueOf(tracktId)).build();
        }

    }

    public static final class Season implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_SEASON).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_SEASON;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_SEASON;

        public static final String TABLE_NAME = "seasons";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TVDB_ID = "tvdb_id";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_TMDB_ID = "tmdb_id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_EPISODE_COUNT = "episode_count";
        public static final String COLUMN_AIRED_EPISODE = "aired_episodes";
        public static final String COLUMN_SHOW_ID = "show_trakt_id";

        public static final int POSITION_ID = 0;
        public static final int POSITION_TITLE = 1;
        public static final int POSITION_TVDB_ID = 2;
        public static final int POSITION_IMDB_ID = 3;
        public static final int POSITION_TMDB_ID = 4;
        public static final int POSITION_OVERVIEW = 5;
        public static final int POSITION_NUMBER = 6;
        public static final int POSITION_FIRST_AIRED = 7;
        public static final int POSITION_RATING = 8;
        public static final int POSITION_VOTES = 9;
        public static final int POSITION_EPISODE_COUNT = 10;
        public static final int POSITION_AIRED_EPISODES = 11;
        public static final int POSITION_SHOW_ID = 12;
        public static final int POSITION_WATCHED = 13;

        public static final String[] COLUMNS = new String[]{
                TABLE_NAME + "." + _ID
                , TABLE_NAME + "." + COLUMN_TITLE
                , TABLE_NAME + "." + COLUMN_TVDB_ID
                , TABLE_NAME + "." + COLUMN_IMDB_ID
                , TABLE_NAME + "." + COLUMN_TMDB_ID
                , TABLE_NAME + "." + COLUMN_NUMBER
                , TABLE_NAME + "." + COLUMN_OVERVIEW
                , TABLE_NAME + "." + COLUMN_FIRST_AIRED
                , TABLE_NAME + "." + COLUMN_RATING
                , TABLE_NAME + "." + COLUMN_VOTES
                , TABLE_NAME + "." + COLUMN_EPISODE_COUNT
                , TABLE_NAME + "." + COLUMN_AIRED_EPISODE
                , TABLE_NAME + "." + COLUMN_SHOW_ID
        };

        public static Uri makeUriWithId(Integer tracktId) {
            return URI.buildUpon().appendPath(String.valueOf(tracktId)).build();
        }
    }
}
