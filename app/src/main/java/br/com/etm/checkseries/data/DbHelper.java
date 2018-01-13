package br.com.etm.checkseries.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eduardo on 07/01/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "CheckSeries.db";
    private static final int VERSION = 1;

    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SHOW_TABLE = getSQLCreateShowTable();
        final String SQL_CREATE_SEASON_TABLE = getSQLCreateSeasonTable();
        final String SQL_CREATE_EPISODE_TABLE = getSQLCreateEpisodeTable();

        db.execSQL(SQL_CREATE_SHOW_TABLE);
        db.execSQL(SQL_CREATE_SEASON_TABLE);
        db.execSQL(SQL_CREATE_EPISODE_TABLE);
    }

    private String getSQLCreateShowTable() {
        return "CREATE TABLE " + Contract.Show.TABLE_NAME + "( "
                + Contract.Show._ID + " INTEGER PRIMARY KEY, "
                + Contract.Show.COLUMN_NAME + " TEXT , "
                + Contract.Show.COLUMN_TVDB_ID + " TEXT , "
                + Contract.Show.COLUMN_IMDB_ID + " TEXT , "
                + Contract.Show.COLUMN_TMDB_ID + " TEXT , "
                + Contract.Show.COLUMN_YEAR + " INTEGER , "
                + Contract.Show.COLUMN_TYPE + " TEXT , "
                + Contract.Show.COLUMN_BACKGROUND_URL + " TEXT , "
                + Contract.Show.COLUMN_BANNER_URL + " TEXT , "
                + Contract.Show.COLUMN_POSTER_URL + " TEXT , "
                + Contract.Show.COLUMN_OVERVIEW + " TEXT , "
                + Contract.Show.COLUMN_FIRST_AIRED + " TEXT , "
                + Contract.Show.COLUMN_AIR_DATE + " TEXT , "
                + Contract.Show.COLUMN_AIR_TIME + " TEXT , "
                + Contract.Show.COLUMN_RUNTIME + " INTEGER , "
                + Contract.Show.COLUMN_NETWORK + " TEXT , "
                + Contract.Show.COLUMN_COUNTRY + " TEXT , "
                + Contract.Show.COLUMN_UPDATED_AT + " TEXT , "
                + Contract.Show.COLUMN_TRAILER + " TEXT , "
                + Contract.Show.COLUMN_HOMEPAGE + " TEXT , "
                + Contract.Show.COLUMN_STATUS + " TEXT , "
                + Contract.Show.COLUMN_RATING + " TEXT , "
                + Contract.Show.COLUMN_VOTES + " INTEGER , "
                + Contract.Show.COLUMN_COMMENT_COUNT + " INTEGER , "
                + Contract.Show.COLUMN_GENRES + " TEXT , "
                + Contract.Show.COLUMN_FAVOURITE + " BOOLEAN , "
                + Contract.Show.COLUMN_HIDDEN + " BOOLEAN , "
                + Contract.Show.COLUMN_UNFINISHED + " BOOLEAN , "
                + Contract.Show.COLUMN_TOTAL_EPISODES + " INTEGER "
                + ")";
    }

    private String getSQLCreateSeasonTable() {
        return "CREATE TABLE " + Contract.Season.TABLE_NAME + "( "
                + Contract.Season._ID + " INTEGER PRIMARY KEY, "
                + Contract.Season.COLUMN_TITLE + " TEXT , "
                + Contract.Season.COLUMN_NUMBER + " INTEGER , "
                + Contract.Season.COLUMN_TVDB_ID + " TEXT , "
                + Contract.Season.COLUMN_IMDB_ID + " TEXT , "
                + Contract.Season.COLUMN_TMDB_ID + " TEXT , "
                + Contract.Season.COLUMN_OVERVIEW + " TEXT , "
                + Contract.Season.COLUMN_FIRST_AIRED + " TEXT , "
                + Contract.Season.COLUMN_RATING + " DOUBLE , "
                + Contract.Season.COLUMN_VOTES + " INTEGER , "
                + Contract.Season.COLUMN_EPISODE_COUNT + " INTEGER , "
                + Contract.Season.COLUMN_AIRED_EPISODE + " INTEGER , "
                + Contract.Season.COLUMN_SHOW_ID + " INTEGER "
                + ")";
    }

    private String getSQLCreateEpisodeTable() {
        return "CREATE TABLE " + Contract.Episode.TABLE_NAME + "( "
                + Contract.Episode._ID + " INTEGER PRIMARY KEY, "
                + Contract.Episode.COLUMN_TITLE + " TEXT , "
                + Contract.Episode.COLUMN_TVDB_ID + " TEXT , "
                + Contract.Episode.COLUMN_IMDB_ID + " TEXT , "
                + Contract.Episode.COLUMN_TMDB_ID + " TEXT , "
                + Contract.Episode.COLUMN_OVERVIEW + " TEXT , "
                + Contract.Episode.COLUMN_SEASON + " INTEGER , "
                + Contract.Episode.COLUMN_NUMBER_ABS + " INTEGER , "
                + Contract.Episode.COLUMN_NUMBER + " INTEGER , "
                + Contract.Episode.COLUMN_COMMENT_COUNT + " INTEGER , "
                + Contract.Episode.COLUMN_VOTES + " INTEGER , "
                + Contract.Episode.COLUMN_FIRST_AIRED + " TEXT , "
                + Contract.Episode.COLUMN_RATING + " TEXT , "
                + Contract.Episode.COLUMN_RUNTIME + " INTEGER , "
                + Contract.Episode.COLUMN_AVAILABLE_TRANSLATIONS + " TEXT , "
                + Contract.Episode.COLUMN_UPDATED_AT + " TEXT , "
                + Contract.Episode.COLUMN_BACKGROUND_URL + " TEXT , "
                + Contract.Episode.COLUMN_WATCHED + " INTEGER , "
                + Contract.Episode.COLUMN_SEASON_ID + " INTEGER "
                + ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Show.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Season.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Episode.TABLE_NAME);
        onCreate(db);
    }
}
