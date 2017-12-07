package br.com.etm.checkseries.deprecated.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Language;

/**
 * Created by EDUARDO_MARGOTO on 30/10/2015.
 */
public class DAO_EnvironmentConfig {
    private final static String TABLE = "ENVIRONMENT_CONFIG";
    private final static String[] ALL_COLUNMS = new String[]{"id", "filter_favorite", "filter_hidden", "filter_comingsoon", "filter_unattended", "order_name", "imageOnlyWifi",
            "language_id", "formatNumber", "updateAutomatic", "hiddenEpisodesSpecials", "timeOffset", "notification", "order_nextepisode", "checkmain", "nextupdate", "ntf_only_favorite", "layout_compat"};
    private SQLiteDatabase db;
    private Context context;

    public DAO_EnvironmentConfig(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();

    }

    private ContentValues putValues() {
        ContentValues values = new ContentValues();

        values.put(ALL_COLUNMS[1], EnvironmentConfig.getInstance().isFilter_favorite() ? "1" : "0");
        values.put(ALL_COLUNMS[2], EnvironmentConfig.getInstance().isFilter_hidden() ? "1" : "0");
        values.put(ALL_COLUNMS[3], EnvironmentConfig.getInstance().isFilter_comingsoon() ? "1" : "0");
        values.put(ALL_COLUNMS[4], EnvironmentConfig.getInstance().isFilter_notfinalized() ? "1" : "0");
        values.put(ALL_COLUNMS[5], EnvironmentConfig.getInstance().isOrder_name() ? "1" : "0");
        values.put(ALL_COLUNMS[6], EnvironmentConfig.getInstance().isImageOnlyWifi() ? "1" : "0");
        if (EnvironmentConfig.getInstance().getLanguage() != null)
            values.put(ALL_COLUNMS[7], EnvironmentConfig.getInstance().getLanguage().getId());
        else values.put(ALL_COLUNMS[7], 7); // English default

        values.put(ALL_COLUNMS[8], EnvironmentConfig.getInstance().getFormatNumber());
        values.put(ALL_COLUNMS[9], EnvironmentConfig.getInstance().isUpdateAutomatic() ? "1" : "0");
        values.put(ALL_COLUNMS[10], EnvironmentConfig.getInstance().isHiddenEpisodesSpecials() ? "1" : "0");
        values.put(ALL_COLUNMS[11], EnvironmentConfig.getInstance().getTimeOffset());
        values.put(ALL_COLUNMS[12], EnvironmentConfig.getInstance().isNotification() ? "1" : "0");
        values.put(ALL_COLUNMS[13], EnvironmentConfig.getInstance().isOrder_nextEpisode() ? "1" : "0");
        values.put(ALL_COLUNMS[14], EnvironmentConfig.getInstance().isCheckmain() ? "1" : "0");
        values.put(ALL_COLUNMS[15], EnvironmentConfig.getInstance().getNextUpdate());
        values.put(ALL_COLUNMS[16], EnvironmentConfig.getInstance().isNotification_only_favorite() ? "1" : "0");
        values.put(ALL_COLUNMS[17], EnvironmentConfig.getInstance().isLayoutCompat() ? "1" : "0");
        return values;
    }

    public void create() {
        try {
            if (this.bind() == false) {
                Log.i("LOG-INSERT-EC", "this.bind() == false");
                db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();

                db.insert(TABLE, null, putValues());
            } else Log.i("LOG-INSERT-EC", "this.bind() == true");
//            db.close();
            // atualiza a language
            EnvironmentConfig.getInstance().setLanguage(new DAO_Language(context).find(String.valueOf(EnvironmentConfig.getInstance().getLanguage().getId())));
        } catch (Exception e) {
            Log.i("LOG-EXCEPTION-EB_EC", e.toString());
        }
    }

    public void edit() {
        try {
            db.update(TABLE, putValues(), null, null);
//            db.close();
        } catch (Exception e) {
            Log.i("LOG-EXCEPTION-DB_EC", e.toString());
        }
    }

    public void remove() {
        db.delete(TABLE, "id = " + EnvironmentConfig.getInstance().getId(), null);
//        db.close();
    }

    private boolean getValue(Cursor cursor) {

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            EnvironmentConfig.getInstance().setId(cursor.getInt(0));

            if (cursor.getString(1).equals("0"))
                EnvironmentConfig.getInstance().setFilter_favorite(false);
            else EnvironmentConfig.getInstance().setFilter_favorite(true);

            if (cursor.getString(2).equals("0"))
                EnvironmentConfig.getInstance().setFilter_hidden(false);
            else EnvironmentConfig.getInstance().setFilter_hidden(true);

            if (cursor.getString(3).equals("0"))
                EnvironmentConfig.getInstance().setFilter_comingsoon(false);
            else EnvironmentConfig.getInstance().setFilter_comingsoon(true);

            if (cursor.getString(4).equals("0"))
                EnvironmentConfig.getInstance().setFilter_notfinalized(false);
            else EnvironmentConfig.getInstance().setFilter_notfinalized(true);

            if (cursor.getString(5).equals("0"))
                EnvironmentConfig.getInstance().setOrder_name(false);
            else EnvironmentConfig.getInstance().setOrder_name(true);

            if (cursor.getString(6).equals("0"))
                EnvironmentConfig.getInstance().setImageOnlyWifi(false);
            else EnvironmentConfig.getInstance().setImageOnlyWifi(true);

            EnvironmentConfig.getInstance().setLanguage(new Language(cursor.getInt(7)));
            EnvironmentConfig.getInstance().setFormatNumber(cursor.getString(8));

            if (cursor.getString(9).equals("0"))
                EnvironmentConfig.getInstance().setUpdateAutomatic(false);
            else EnvironmentConfig.getInstance().setUpdateAutomatic(true);

            if (cursor.getString(10).equals("0"))
                EnvironmentConfig.getInstance().setHiddenEpisodesSpecials(false);
            else EnvironmentConfig.getInstance().setHiddenEpisodesSpecials(true);

            EnvironmentConfig.getInstance().setTimeOffset(cursor.getInt(11));

            if (cursor.getString(12).equals("0"))
                EnvironmentConfig.getInstance().setNotification(false);
            else EnvironmentConfig.getInstance().setNotification(true);

            if (cursor.getString(13).equals("0"))
                EnvironmentConfig.getInstance().setOrder_nextEpisode(false);
            else EnvironmentConfig.getInstance().setOrder_nextEpisode(true);

            if (cursor.getString(14) == null) {
                EnvironmentConfig.getInstance().setCheckmain(false);
            } else if (cursor.getString(14).equals("0"))
                EnvironmentConfig.getInstance().setCheckmain(false);
            else EnvironmentConfig.getInstance().setCheckmain(true);

            Long l = cursor.getLong(15);
            if (l != null)
                EnvironmentConfig.getInstance().setNextUpdate(l);
            else EnvironmentConfig.getInstance().setNextUpdate(-1);

            if (cursor.getString(16) != null) {
                if (cursor.getString(16).equals("0"))
                    EnvironmentConfig.getInstance().setNotification_only_favorite(false);
                else EnvironmentConfig.getInstance().setNotification_only_favorite(true);
            }

            if (cursor.getString(17) != null) {
                if (cursor.getString(17).equals("0"))
                    EnvironmentConfig.getInstance().setLayoutCompat(false);
                else EnvironmentConfig.getInstance().setLayoutCompat(true);
            }

            return true;

        }
        return false;
    }


    public boolean bind() {
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, null);

        boolean haveValue = getValue(cursor);
       // db.close();
        return haveValue;
    }
}
