package br.com.etm.checkseries.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.domains.ListOfUser;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class DAO_List {
    private final static String TABLE = "LISTS";
    private final static String[] ALL_COLUNMS = new String[]{"id", "name_list", "weight"};

    private SQLiteDatabase db;
    private Context context;

    public DAO_List(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues(ListOfUser list) {
        ContentValues values = new ContentValues();

//        values.put(ALL_COLUNMS[0], list.getId());
        values.put(ALL_COLUNMS[1], list.getName());
        values.put(ALL_COLUNMS[2], list.getWeight());

        return values;
    }

    public int create(ListOfUser lofu) {
        long id = db.insert(TABLE, null, putValues(lofu));
        lofu.setId(Integer.parseInt(String.valueOf(id)));
//        db.close();

        return Integer.parseInt(String.valueOf(id));
    }

    public void edit(ListOfUser lofu) {
        db.update(TABLE, putValues(lofu), "id = " + lofu.getId(), null);
//        db.close();
    }

    public void remove(ListOfUser lofu) {
        db.delete(TABLE, "id = " + lofu.getId(), null);

//        db.close();
    }

    private List<ListOfUser> getValues(Cursor cursor) {
        List<ListOfUser> aux = new ArrayList<>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ListOfUser lofu = new ListOfUser();

                lofu.setId(cursor.getInt(0));
                lofu.setName(cursor.getString(1));
                lofu.setWeight(cursor.getInt(2));
                aux.add(lofu);
            } while (cursor.moveToNext());
        }
        return aux;
    }

    public ListOfUser find(String id) {
        ArrayList<ListOfUser> lofus = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        lofus.addAll(getValues(cursor));
//        db.close();
        return lofus.get(0);
    }

    public ArrayList<ListOfUser> findAll() {
        ArrayList<ListOfUser> lOfus = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, "weight ASC");

        lOfus.addAll(getValues(cursor));
//        db.close();
        return lOfus;
    }

}
