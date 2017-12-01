package br.com.etm.checkseries.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class BDCore extends SQLiteOpenHelper {

    public static final String NOME_BD = "ambiserie_bd";
    private static final Integer VERSION_BD = 32;
    private static BDCore bdCore;

    private BDCore(Context context) {
        super(context, NOME_BD, null, VERSION_BD);
    }

    public static BDCore getInstance(Context context){
        if(bdCore == null)
            bdCore = new BDCore(context);

        return bdCore;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("LOG-BDCORE", "onCreate");
        db.execSQL("CREATE TABLE PROFILE (id text primary key, " +
                "name text not null," +
                "username text not null," +
                "image_url text not null," +
                "email text not null," +
                "language text not null  );");

        db.execSQL("CREATE TABLE ENVIRONMENT_CONFIG (id integer not null primary key autoincrement, " +
                "filter_favorite bit not null," +
                "filter_hidden bit not null," +
                "filter_comingsoon bit not null," +
                "filter_unattended bit not null," +
                "order_name bit not null," +
                "imageOnlyWifi bit not null," +
                "language_id INTEGER not null," +
                "formatNumber text not null," +
                "updateAutomatic bit not null," +
                "hiddenEpisodesSpecials bit not null," +
                "timeOffset integer not null," +
                "notification bit not null," +
                "order_nextepisode bit not null," +
                "checkmain bit," +
                "nextupdate long," +
                "ntf_only_favorite bit, " +
                "layout_compat bit);");


        db.execSQL("CREATE TABLE SERIES (id integer, " +
                "serieid integer not null," +
                "name text not null," +
                "aliasname text not null," +
                "overview text not null," +
                "first_aired datetime," +
                "banner text," +
                "poster text," +
                "fanArt text," +
                "imdb_id text," +
                "zap2it_id text," +
                "runtime text," +
                "language text," +
                "last_updated text," +
                "status text," +
                "actors text," +
                "airs_DayOfWeek text," +
                "airs_Time text," +
                "genre text," +
                "network text," +
                "favorite bit," +
                "hidden bit," +
                "rating text," +
                "ratingCount integer  );");

        db.execSQL("CREATE TABLE EPISODES (id integer, " +
                "episodeNumber integer not null," +
                "seasonNumber integer not null," +
                "serieId integer not null," +
                "director text not null," +
                "epImgFlag text not null," +
                "episodeName text not null," +
                "firstAired datetime," +
                "thumbAdded datetime," +
                "imdb_id text," +
                "language text," +
                "overview text," +
                "writer text," +
                "thumbHight text," +
                "thumbWidth text," +
                "seasonId text," +
                "lastUpdated text," +
                "filename text," +
                "airBeforeSeason text," +
                "airBeforeEpisode text," +
                "airAfterSeason text," +
                "watched bit not null," +
                "skipped bit not null," +
                "totalEpisodeNumber integer, " +
                "dateWatched datetime );");


        db.execSQL("CREATE TABLE LISTS (id integer primary key autoincrement, " +
                "name_list text not null," +
                "weight integer not null);"); //TABLE ADDED IN VERSION 11 - weight(version 15)

        db.execSQL("CREATE TABLE LIST_SERIE (id integer primary key autoincrement, " +
                "serie_id integer not null," +
                "list_id integer not null);"); //TABLE ADDED IN VERSION 11

        db.execSQL("CREATE TABLE LANGUAGES (id integer primary key, " +
                "abbreviation text not null," +
                "language text not null);"); //TABLE ADDED IN VERSION 11


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("LOG-BDCORE", "onUpgrade");
//        db.execSQL("DROP TABLE IF EXISTS PROFILE;");
//        db.execSQL("DROP TABLE IF EXISTS SERIES;");
//        db.execSQL("DROP TABLE IF EXISTS EPISODES;");
//        db.execSQL("DROP TABLE IF EXISTS ENVIRONMENT_CONFIG;");
//        db.execSQL("DROP TABLE IF EXISTS LISTS;");
//        db.execSQL("DROP TABLE IF EXISTS LIST_SERIE;");
//        db.execSQL("DROP TABLE IF EXISTS LANGUAGES;");
//        onCreate(db);

        if (VERSION_BD == 24) {
            db.execSQL("ALTER TABLE EPISODES ADD COLUMN totalEpisodeNumber integer");
        }
        if (VERSION_BD == 26) {
            db.execSQL("ALTER TABLE ENVIRONMENT_CONFIG ADD COLUMN checkmain bit");
        }
        if (VERSION_BD == 27) {
            db.execSQL("ALTER TABLE SERIES ADD COLUMN rating text");
            db.execSQL("ALTER TABLE SERIES ADD COLUMN ratingCount integer");
        }

        if (VERSION_BD == 28) {
            db.execSQL("ALTER TABLE ENVIRONMENT_CONFIG ADD COLUMN nextupdate long");
        }

        if (VERSION_BD == 29) {
            try {
                db.execSQL("ALTER TABLE ENVIRONMENT_CONFIG ADD COLUMN ntf_only_favorite bit");
            } catch (Exception e) {
                Log.e("BD-ERROR", e.getMessage());
            }

        }


        if (VERSION_BD == 32) {
            try {
                db.execSQL("ALTER TABLE EPISODES ADD COLUMN dateWatched datetime");
            } catch (Exception e) {
                Log.e("BD-ERROR", e.getMessage());
            }
            try {
                db.execSQL("ALTER TABLE ENVIRONMENT_CONFIG ADD COLUMN layout_compat bit");
            } catch (Exception e) {
                Log.e("BD-ERROR", e.getMessage());
            }
        }



    }
}
