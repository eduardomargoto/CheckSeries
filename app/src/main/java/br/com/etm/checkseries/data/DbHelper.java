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

    private String getSQLCreateShowTable(){
        return "CREATE TABLE " + Contract.Show.TABLE_NAME + "( "
                + Contract.Show._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Show.COLUMN_NAME + " TEXT NOT NULL " +
                ")";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Show.TABLE_NAME);
        onCreate(db);
    }
}
