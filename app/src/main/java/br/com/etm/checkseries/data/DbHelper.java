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

        db.execSQL(SQL_CREATE_SHOW_TABLE);
    }

    private String getSQLCreateShowTable() {
        return "CREATE TABLE " + Contract.Show.TABLE_NAME + "( "
                + Contract.Show._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Show.COLUMN_NAME + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_TVDB_ID + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_IMDB_ID + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_TMDB_ID + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_YEAR + " INTEGER NOT NULL, "
                + Contract.Show.COLUMN_TYPE + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_LOGO_URL + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_BANNER_URL + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_POSTER_URL + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_FANART_URL + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_FIRST_AIRED + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_AIR_DATE + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_AIR_TIME + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_RUNTIME + " INTEGER NOT NULL, "
                + Contract.Show.COLUMN_NETWORK + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_COUNTRY + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_UPDATED_AT + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_TRAILER + " TEXT , "
                + Contract.Show.COLUMN_HOMEPAGE + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_STATUS + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_RATING + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_VOTES + " INTEGER NOT NULL, "
                + Contract.Show.COLUMN_COMMENT_COUNT + " INTEGER NOT NULL, "
                + Contract.Show.COLUMN_GENRES + " TEXT NOT NULL, "
                + Contract.Show.COLUMN_TOTAL_EPISODES + " INTEGER NOT NULL "
                + ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Show.TABLE_NAME);
        onCreate(db);
    }
}
