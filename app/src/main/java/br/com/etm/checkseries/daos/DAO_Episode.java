package br.com.etm.checkseries.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 31/10/2015.
 */
public class DAO_Episode {
    private final static String TABLE = "EPISODES";
    private final static String[] ALL_COLUNMS = new String[]{"id", "episodeNumber", "seasonNumber", "serieId", "director", "epImgFlag", "episodeName", "firstAired", "thumbAdded", "imdb_id", "language", "overview", "writer",
            "thumbHight", "thumbWidth", "seasonId", "lastUpdated", "filename", "airBeforeSeason", "airBeforeEpisode", "airAfterSeason", "watched", "skipped", "totalEpisodeNumber", "dateWatched"};
    private SQLiteDatabase db;
    private Context context;

    public DAO_Episode(Context context) {
        this.context = context;
        if (db != null) {
            if (db.isOpen())
                db.close();
        }
        db = BDCore.getInstance(context).getWritableDatabase();//new BDCore(context).getWritableDatabase();
    }

    private ContentValues putValues(Episode episode) {
        SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues values = new ContentValues();

        values.put(ALL_COLUNMS[0], episode.getId());
        values.put(ALL_COLUNMS[1], episode.getEpisodeNumber());
        values.put(ALL_COLUNMS[2], episode.getSeasonNumber());
        values.put(ALL_COLUNMS[3], episode.getSerieId());
        values.put(ALL_COLUNMS[4], episode.getDirector());
        values.put(ALL_COLUNMS[5], episode.getEpImgFlag());
        values.put(ALL_COLUNMS[6], episode.getEpisodeName());
        if (episode.getFirstAired() != null)
            values.put(ALL_COLUNMS[7], sdf.format(episode.getFirstAired()));
        if (episode.getThumbAdded() != null)
            values.put(ALL_COLUNMS[8], sdf.format(episode.getThumbAdded()));
        values.put(ALL_COLUNMS[9], episode.getImdb_id());
        values.put(ALL_COLUNMS[10], episode.getLanguage());
        values.put(ALL_COLUNMS[11], episode.getOverview());
        values.put(ALL_COLUNMS[12], episode.getWriter());
        values.put(ALL_COLUNMS[13], episode.getThumbHight());
        values.put(ALL_COLUNMS[14], episode.getThumbWidth());
        values.put(ALL_COLUNMS[15], episode.getSeasonId());
        values.put(ALL_COLUNMS[16], episode.getLastUpdated());
        values.put(ALL_COLUNMS[17], episode.getFilename());
        values.put(ALL_COLUNMS[18], episode.getAirBeforeSeason());
        values.put(ALL_COLUNMS[19], episode.getAirBeforeEpisode());
        values.put(ALL_COLUNMS[20], episode.getAirAfterSeason());
        values.put(ALL_COLUNMS[21], episode.isWatched());
        values.put(ALL_COLUNMS[22], episode.isSkipped());
        values.put(ALL_COLUNMS[23], episode.getTotalEpisodeNumber());
        if (episode.getDateWatched() != null)
            values.put(ALL_COLUNMS[24], sdf_time.format(episode.getDateWatched()));

        return values;
    }

    public void create(Episode episode) {
        db.insert(TABLE, null, putValues(episode));
        //   db.close();
    }

    public void edit(Episode episode) {
        db.update(TABLE, putValues(episode), "id = " + episode.getId(), null);
        //   db.close();
    }

    public void updateWatchedSkipped(Episode episode) {
        episode.setDateWatched(new Date());  // adicionar a data de foi pulado ou assistido o episódio
        db.update(TABLE, putValues(episode), "id = " + episode.getId(), null);
        //   db.close();
    }

    public void remove(Episode episode) {
        UtilsImages.removerImagem(this.context, episode.getFilename().replaceAll("/", "-"), "", true); //episodes/78804/533011.jpg
        db.delete(TABLE, "id = " + episode.getId(), null);

        //  db.close();
    }

    private List<Episode> getValues(Cursor cursor) {
        List<Episode> aux = new ArrayList<>();

        SimpleDateFormat sdf_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Episode epi = new Episode();

                epi.setId(cursor.getInt(0));
                epi.setEpisodeNumber(cursor.getInt(1));
                epi.setSeasonNumber(cursor.getInt(2));
                epi.setSerieId(cursor.getInt(3));
                epi.setDirector(cursor.getString(4));
                epi.setEpImgFlag(cursor.getString(5));
                epi.setEpisodeName(cursor.getString(6));
                try {
                    epi.setFirstAired(sdf.parse(cursor.getString(7)));
                } catch (Exception e) {
                    epi.setFirstAired(null);
                }
                try {
                    epi.setThumbAdded(sdf.parse(cursor.getString(8)));
                } catch (Exception e) {
                    epi.setThumbAdded(null);
                }
                epi.setImdb_id(cursor.getString(9));
                epi.setLanguage(cursor.getString(10));
                epi.setOverview(cursor.getString(11));
                epi.setWriter(cursor.getString(12));
                epi.setThumbHight(cursor.getString(13));
                epi.setThumbWidth(cursor.getString(14));
                epi.setSeasonId(cursor.getString(15));
                epi.setLastUpdated(cursor.getString(16));
                epi.setFilename(cursor.getString(17));
                epi.setAirBeforeSeason(cursor.getString(18));
                epi.setAirBeforeEpisode(cursor.getString(19));
                epi.setAirAfterSeason(cursor.getString(20));
                if (cursor.getString(21).equals("0"))
                    epi.setWatched(false);
                else epi.setWatched(true);

                if (cursor.getString(22).equals("0"))
                    epi.setSkipped(false);
                else epi.setSkipped(true);

                epi.setTotalEpisodeNumber(cursor.getInt(23));
                try {
                    epi.setDateWatched(sdf_time.parse(cursor.getString(24)));
                } catch (Exception e) {
                    epi.setDateWatched(null);
                }
                aux.add(epi);
            } while (cursor.moveToNext());
        }
        return aux;
    }

    public Episode find(String id) throws FileNotFoundException {
        ArrayList<Episode> episodes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "id = " + id, null);

        episodes.addAll(getValues(cursor));
        //   db.close();
        return episodes.get(0);
    }

    public int getCount(String serieId) {

        Cursor cursor;
        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials())
            cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serieId + " AND seasonNumber != 0 ", "seasonNumber, episodeNumber ASC");
        else
            cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serieId, "seasonNumber, episodeNumber ASC");

        return cursor.getCount();
    }

    public Episode findNextEpisode(String serieId) {
        ArrayList<Episode> episodes = new ArrayList<>();

        Cursor cursor;
        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials())
            cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serieId + " AND watched = 0 AND skipped = 0 AND seasonNumber != 0 ", "seasonNumber, episodeNumber ASC");
        else
            cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serieId + " AND watched = 0 AND skipped = 0 ", "seasonNumber, episodeNumber ASC");

        episodes.addAll(getValues(cursor));

        //  db.close();
        try {
            return episodes.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public ArrayList<Episode> findAll() {
        ArrayList<Episode> episodes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, null, null, "seasonNumber ASC");

        episodes.addAll(getValues(cursor));
        db.close();
        return episodes;
    }

    public ArrayList<Episode> findAllHistoric() {
        ArrayList<Episode> episodes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        Calendar yerterday = Calendar.getInstance();
        yerterday.set(Calendar.DAY_OF_MONTH, yerterday.get(Calendar.DAY_OF_MONTH) - 30);
        // HISTORICO DE EPISODIOS DOS ULTIMOS 30 DIAS
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "date(dateWatched) BETWEEN date('" + sdf.format(yerterday.getTime()) + "') AND  date('" + sdf.format(today.getTime()) + "') AND dateWatched IS NOT NULL", "dateWatched DESC, seasonNumber, episodeNumber ASC");

        episodes.addAll(getValues(cursor));
        //   db.close();
        return episodes;
    }

    public ArrayList<Episode> findAll(Serie serie) {
        ArrayList<Episode> episodes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serie.getId(), "seasonNumber, episodeNumber ASC");

        episodes.addAll(getValues(cursor));
        //  db.close();
        return episodes;
    }

    public ArrayList<Episode> findAll(Serie serie, int seasonNumber) {
        ArrayList<Episode> episodes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "serieId = " + serie.getId() + " AND seasonNumber = " + seasonNumber, "seasonNumber, episodeNumber ASC");

        episodes.addAll(getValues(cursor));
        //    db.close();
        return episodes;
    }

    public List<Episode> findAllRelease() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Episode> episodes = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 30);

        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "date(firstAired) BETWEEN date('" + sdf.format(today.getTime()) + "') AND  date('" + sdf.format(tomorrow.getTime()) + "')", "date(firstAired) ASC");

        episodes.addAll(getValues(cursor));
        //  db.close();
        return episodes;
    }

    public List<Episode> findAllToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Episode> episodes = new ArrayList<>();
        Calendar today = Calendar.getInstance();
//        today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + 2); // tomorrow

        Cursor cursor = db.query(TABLE, ALL_COLUNMS, null, null, "id", "date(firstAired) = date('" + sdf.format(today.getTime()) + "')", "firstAired DESC");

        episodes.addAll(getValues(cursor));
        //   db.close();
        return episodes;
    }
}
