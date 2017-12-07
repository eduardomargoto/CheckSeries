package br.com.etm.checkseries.deprecated.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.deprecated.domains.ListOfUser;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class DAO_Serie {
    private final static String TABLE = "SERIES";
    private final static String[] ALL_COLUNMS = new String[]{"id", "serieid", "name", "aliasname", "overview", "first_aired", "banner", "poster", "fanArt", "imdb_id", "zap2it_id", "runtime", "language",
            "status", "actors", "airs_DayOfWeek", "airs_Time", "genre", "last_updated", "network", "favorite", "hidden"};
    private SQLiteDatabase db;
    private Context context;

    public DAO_Serie(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues(Serie serie) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues values = new ContentValues();

        values.put("id", serie.getId());
        if (serie.getSerieid() != null)
            values.put("serieid", serie.getSerieid());
        else values.put("serieid", 0);
        values.put("name", serie.getName());
        values.put("aliasname", serie.getAlias_names());
        values.put("overview", serie.getOverview());
        if (serie.getFirst_aired() != null)
            values.put("first_aired", sdf.format(serie.getFirst_aired()));
        values.put("banner", serie.getBanner());
        values.put("poster", serie.getPoster());
        values.put("fanArt", serie.getFanArt());
        values.put("imdb_id", serie.getImdb_id());
        values.put("zap2it_id", serie.getZap2it_id());
        values.put("runtime", serie.getRuntime());
        values.put("language", serie.getLanguage());
        values.put("status", serie.getStatus());
        values.put("actors", serie.getActors());
        values.put("airs_DayOfWeek", serie.getAirs_DayOfWeek());
        values.put("airs_Time", serie.getAirs_Time());
        values.put("genre", serie.getGenre());
        values.put("last_updated", serie.getLast_updated());
        values.put("network", serie.getNetwork());
        values.put("favorite", serie.isFavorite());
        values.put("hidden", serie.isHidden());
        return values;
    }

    public void create(Serie serie) {
        db.insert(TABLE, null, putValues(serie));
     //   db.close();
    }

    public void edit(Serie serie) {
        db.update(TABLE, putValues(serie), "id = " + serie.getId(), null);
    //    db.close();
    }

    public void remove(Serie serie) {
        UtilsImages.removerImagem(this.context, serie.getPoster().replace(APITheTVDB.PATH_POSTERS_SERIE, ""), "", true);
        UtilsImages.removerImagem(this.context, serie.getFanArt().replaceAll("/", "-"), "", true);
        UtilsImages.removerImagem(this.context, serie.getBanner().replaceAll("/", "-"), "", true);
        db.delete(TABLE, "id = " + serie.getId(), null);

     //   db.close();
    }

    private List<Serie> getValues(Cursor cursor) {
        List<Serie> aux = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Serie s = new Serie();

                s.setId(cursor.getInt(0));
                s.setSerieid(cursor.getInt(1));
                s.setName(cursor.getString(2));
                s.setAlias_names(cursor.getString(3));
                s.setOverview(cursor.getString(4));
                try {
                    s.setFirst_aired(sdf.parse(cursor.getString(5)));
                } catch (Exception e) {
                    s.setFirst_aired(null);
                }
                s.setBanner(cursor.getString(6));
                s.setPoster(cursor.getString(7));
                s.setFanArt(cursor.getString(8));
                s.setImdb_id(cursor.getString(9));
                s.setZap2it_id(cursor.getString(10));
                s.setRuntime(cursor.getString(11));
                s.setLanguage(cursor.getString(12));
                s.setStatus(cursor.getString(13));
                s.setActors(cursor.getString(14));
                s.setAirs_DayOfWeek(cursor.getString(15));
                s.setAirs_Time(cursor.getString(16));
                s.setGenre(cursor.getString(17));
                s.setLast_updated(cursor.getString(18));
                s.setNetwork(cursor.getString(19));
                if (cursor.getString(20).equals("0"))
                    s.setFavorite(false);
                else s.setFavorite(true);
                if (cursor.getString(21).equals("0"))
                    s.setHidden(false);
                else s.setHidden(true);


                aux.add(s);
            } while (cursor.moveToNext());
        }
       // cursor.close();
        return aux;
    }

    public Serie find(String id) {
        ArrayList<Serie> serieList = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        serieList.addAll(getValues(cursor));
      //  db.close();
        return serieList.get(0);
    }

    public String findName(Integer serieId) {
        String name = "";
        Cursor cursor = db.query(TABLE, new String[]{"name"}, null, null, "id", "id = " + serieId.toString(), null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(0);
        }

        cursor.close();
     //   db.close();
        return name;
    }

//    public ArrayList<Serie> findAll(boolean orderName, boolean orderNextEpisode) {
//        ArrayList<Serie> serieList = new ArrayList<>();
//        Cursor cursor = null;
//        if (orderName) {
//            if (!EnvironmentConfig.getInstance().isHiddenEpisodesSpecials())
//                cursor = db.rawQuery("SELECT SERIES.* FROM SERIES, EPISODES WHERE SERIES.id = EPISODES.serieId ORDER BY SERIES.favorite DESC, SERIES.name ASC", null);
//            else  cursor = db.rawQuery("SELECT SERIES.* FROM SERIES, EPISODES WHERE SERIES.id = EPISODES.serieId AND EPISODES.seasonNumber <> 0 ORDER BY SERIES.favorite DESC, SERIES.name ASC", null);
//        }else if (orderNextEpisode) {
//            if (!EnvironmentConfig.getInstance().isHiddenEpisodesSpecials())
//                cursor = db.rawQuery("SELECT SERIES.* FROM SERIES, EPISODES WHERE SERIES.id = EPISODES.serieId AND (EPISODES.watched = 0 OR EPISODES.skipped = 0) GROUP BY SERIES.id ORDER BY SERIES.favorite DESC, EPISODES.firstAired ASC", null);
//            else cursor = db.rawQuery("SELECT SERIES.* FROM SERIES, EPISODES WHERE SERIES.id = EPISODES.serieId AND (EPISODES.watched = 0 OR EPISODES.skipped = 0) AND EPISODES.seasonNumber <> 0 GROUP BY SERIES.id ORDER BY SERIES.favorite DESC, EPISODES.firstAired ASC", null);
//        }
////        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, "", null);
//        if (cursor != null)
//            serieList.addAll(getValues(cursor));
//
//        db.close();
//        return serieList;
//    }

    public ArrayList<Serie> findAll() {
        ArrayList<Serie> serieList = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, "favorite desc");

        serieList.addAll(getValues(cursor));
        //db.close();
        return serieList;
    }

    public ArrayList<Serie> findAll(ListOfUser list) {
        ArrayList<Serie> serieList = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, "", null);

        serieList.addAll(getValues(cursor));
      //  db.close();
        return serieList;
    }

    public List<Serie> findAllFavorites() {
        ArrayList<Serie> serieList = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "favorite = 1", null);

        serieList.addAll(getValues(cursor));
      //  db.close();
        return serieList;
    }
}
