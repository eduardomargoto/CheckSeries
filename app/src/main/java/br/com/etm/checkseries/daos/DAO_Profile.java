package br.com.etm.checkseries.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.etm.checkseries.domains.Profile;

/**
 * Created by EDUARDO_MARGOTO on 31/10/2015.
 */
public class DAO_Profile {
    private final static String TABLE = "PROFILE";
    private final static String[] ALL_COLUNMS = new String[]{"id", "name", "username", "image_url", "email", "language"};
    private SQLiteDatabase db;
    private Context context;

    public DAO_Profile(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues() {
        ContentValues values = new ContentValues();

        values.put(ALL_COLUNMS[0], Profile.getInstance().getId());
        values.put(ALL_COLUNMS[1], Profile.getInstance().getName());
        values.put(ALL_COLUNMS[2], Profile.getInstance().getUsername());
        values.put(ALL_COLUNMS[3], Profile.getInstance().getImageUrl());
        values.put(ALL_COLUNMS[4], Profile.getInstance().getEmail());
        values.put(ALL_COLUNMS[5], Profile.getInstance().getLanguage());
        return values;
    }

    public void create() {
        try {
            if (this.bind() == false) {
                Log.i("LOG-INSERT-PROFILE", "this.bind() == false");
                db.insert(TABLE, null, putValues());
            } else Log.i("LOG-INSERT-PROFILE", "this.bind() == true");
//            db.close();
        } catch (Exception e) {
            Log.i("LOG-EXCEPTION-PROFILE", e.toString());
        }
  /*      db.insert(TABLE, null, putValues());
        db.close();*/
    }

    public boolean bind() {
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, null);

        boolean haveValue = getValues(cursor);
//        db.close();
        return haveValue;
    }

    public void edit() {
        db.update(TABLE, putValues(), "id = " + Profile.getInstance().getId(), null);
//        db.close();
    }

    public void remove() {
        db.delete(TABLE, null, null);
        Profile.getInstance().clear();
//        db.close();
    }

    private boolean getValues(Cursor cursor) {

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();


            Profile.getInstance().setId(cursor.getString(0));
            Profile.getInstance().setName(cursor.getString(1));
            Profile.getInstance().setUsername(cursor.getString(2));
            Profile.getInstance().setImageUrl(cursor.getString(3));
            Profile.getInstance().setEmail(cursor.getString(4));
            Profile.getInstance().setLanguage(cursor.getString(5));

            return true;
        }
        return false;
    }

   /* public Profile find(String id) {
        ArrayList<Profile> episodes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        episodes.addAll(getValues(cursor));
        db.close();
        return episodes.get(0);
    }*/


    /*public ArrayList<Profile> findAll() throws FileNotFoundException {
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, null);

        profiles.addAll(getValues(cursor));
        db.close();
        return profiles;
    }*/

}
