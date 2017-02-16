package br.com.etm.checkseries.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.domains.Language;

/**
 * Created by EDUARDO_MARGOTO on 07/11/2015.
 */
public class DAO_Language {
    private final static String TABLE = "LANGUAGES";
    private final static String[] ALL_COLUNMS = new String[]{"id", "abbreviation", "language"};

    private SQLiteDatabase db;
    private Context context;

    public DAO_Language(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues(Language list) {
        ContentValues values = new ContentValues();

        values.put(ALL_COLUNMS[0], list.getId());
        values.put(ALL_COLUNMS[1], list.getAbbreviation());
        values.put(ALL_COLUNMS[2], list.getLanguage());

        return values;
    }

    public int create(Language language) {
        long id = db.insert(TABLE, null, putValues(language));
        language.setId(Integer.parseInt(String.valueOf(id)));
       // db.close();

        return Integer.parseInt(String.valueOf(id));
    }

    public void edit(Language language) {
        db.update(TABLE, putValues(language), "id = " + language.getId(), null);
      //  db.close();
    }

    public void remove(Language language) {
        db.delete(TABLE, "id = " + language.getId(), null);

      //  db.close();
    }

    private List<Language> getValues(Cursor cursor) {
        List<Language> aux = new ArrayList<>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Language language = new Language();

                language.setId(cursor.getInt(0));
                language.setAbbreviation(cursor.getString(1));
                language.setLanguage(cursor.getString(2));
                aux.add(language);
            } while (cursor.moveToNext());
        }
        return aux;
    }

    public Language find(String id) {
        ArrayList<Language> languages = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        languages.addAll(getValues(cursor));
      //  db.close();
        return languages.get(0);
    }

    public ArrayList<Language> findAll() {
        ArrayList<Language> languages = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, null);

        languages.addAll(getValues(cursor));
      //  db.close();
        return languages;
    }
}
