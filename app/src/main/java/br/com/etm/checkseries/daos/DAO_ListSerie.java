package br.com.etm.checkseries.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.domains.ListOfUser_Serie;
import br.com.etm.checkseries.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class DAO_ListSerie {
    private final static String TABLE = "LIST_SERIE";
    private final static String[] ALL_COLUNMS = new String[]{"id", "serie_id", "list_id"};

    private SQLiteDatabase db;
    private Context context;

    public DAO_ListSerie(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues(ListOfUser_Serie lou_serie) {
        ContentValues values = new ContentValues();

//        values.put(ALL_COLUNMS[0], lou_serie.getId());
        values.put(ALL_COLUNMS[1], lou_serie.getSerie().getId());
        values.put(ALL_COLUNMS[2], lou_serie.getListOfUser().getId());

        return values;
    }

    public void create(ListOfUser_Serie lou_serie) {
        long id = db.insert(TABLE, null, putValues(lou_serie));
        lou_serie.setId(Integer.parseInt(String.valueOf(id)));
//        db.close();
    }

    public void edit(ListOfUser_Serie lou_serie) {
        db.update(TABLE, putValues(lou_serie), "id = " + lou_serie.getId(), null);
//        db.close();
    }

    public void remove(ListOfUser_Serie lou_serie) {
        db.delete(TABLE, "id = " + lou_serie.getId(), null);

//        db.close();
    }

    public void remove(ListOfUser lofu) {
        db.delete(TABLE, "list_id = " + lofu.getId(), null);

//        db.close();
    }

    public void remove(Serie serie) {
        db.delete(TABLE, "serie_id = " + serie.getId(), null);

//        db.close();
    }

    public void remove(ListOfUser lofu, Serie serie) {
        db.delete(TABLE, "list_id = " + lofu.getId() + " AND serie_id = " + serie.getId(), null);

//        db.close();
    }

    private List<ListOfUser_Serie> getValues(Cursor cursor) {
        List<ListOfUser_Serie> aux = new ArrayList<>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ListOfUser_Serie lou_serie = new ListOfUser_Serie();
                Serie s = new Serie();
                ListOfUser listOfUser = new ListOfUser();

                lou_serie.setId(cursor.getInt(0));
                s.setId(cursor.getInt(1));
                listOfUser.setId(cursor.getInt(2));

                lou_serie.setSerie(s);
                lou_serie.setListOfUser(listOfUser);
                aux.add(lou_serie);
            } while (cursor.moveToNext());
        }
        return aux;
    }

    public ListOfUser_Serie find(ListOfUser listOfUser, Serie s)  {
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "list_id = " + listOfUser.getId() + " AND serie_id = " + s.getId(), null);

        lou_serie_list.addAll(getValues(cursor));
//        db.close();
        if (lou_serie_list.isEmpty())
            return null;
        return lou_serie_list.get(0);
    }

    public List<Serie> findAll(ListOfUser listOfUser){
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        ArrayList<Serie> series = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "list_id = " + listOfUser.getId(), null);
        lou_serie_list.addAll(getValues(cursor));
        db.close();

        for (ListOfUser_Serie l : lou_serie_list) {
            series.add(new DAO_Serie(context).find(String.valueOf(l.getSerie().getId())));
        }

        return series;
    }

    public List<ListOfUser_Serie> findAll(Serie serie) {
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        ArrayList<Serie> series = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serie_id = " + serie.getId(), null);
        lou_serie_list.addAll(getValues(cursor));

//        db.close();
        return lou_serie_list;
    }

    public List<ListOfUser_Serie> findAll_by_listOfUser(ListOfUser listOfUser) {
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "list_id = " + listOfUser.getId(), null);
        lou_serie_list.addAll(getValues(cursor));
//        db.close();
        return lou_serie_list;
    }

    public ListOfUser_Serie find(String id) {
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        lou_serie_list.addAll(getValues(cursor));
//        db.close();
        if (lou_serie_list.isEmpty())
            return null;
        return lou_serie_list.get(0);
    }

    public ArrayList<ListOfUser_Serie> findAll(){
        ArrayList<ListOfUser_Serie> lou_serie_list = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, null);

        lou_serie_list.addAll(getValues(cursor));
//        db.close();
        return lou_serie_list;
    }


}
