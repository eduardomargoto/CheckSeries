package br.com.etm.checkseries.data;

import android.database.Cursor;

import com.google.common.collect.ObjectArrays;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.data.trakTv.ApiEpisode;
import br.com.etm.checkseries.api.data.trakTv.ApiMediaObject;
import br.com.etm.checkseries.api.data.trakTv.ApiSeason;
import br.com.etm.checkseries.api.data.trakTv.ApiShow;
import io.reactivex.Observable;

/**
 * Created by eduardo on 11/01/18.
 */

public class DbInteractorImpl implements DbInteractor {

    public DbInteractorImpl() {
    }

    @Override
    public int updateShow(ApiShow apiShow) {
        return App.getContext().getContentResolver()
                .update(Contract.Show.URI
                        , apiShow.getContentValues()
                        , Contract.Show._ID + " = ?"
                        , new String[]{String.valueOf(apiShow.getTraktId())});
    }

    @Override
    public Observable<List<ApiShow>> retrieveShows(String where, String order) {

        Cursor cursor = App.getContext().getContentResolver()
                .query(Contract.Show.URI,
                        Contract.Show.SHOWS_COLUMNS
                        , where
                        , null
                        , order);

        List<ApiShow> apiShows = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ApiShow apiShow = new ApiShow(cursor);

                Cursor cursorSeason = App.getContext().getContentResolver()
                        .query(Contract.Season.URI,
                                Contract.Season.COLUMNS
                                , Contract.Season.COLUMN_SHOW_ID + " = ?"
                                , new String[]{String.valueOf(apiShow.getTraktId())}
                                , Contract.Season.COLUMN_NUMBER);

                if (cursorSeason != null) {
                    while (cursorSeason.moveToNext()) {
                        ApiSeason apiSeason = new ApiSeason(cursorSeason);

                        Cursor cursorEpisode = App.getContext().getContentResolver()
                                .query(Contract.Episode.URI,
                                        Contract.Episode.COLUMNS
                                        , Contract.Episode.COLUMN_SEASON_ID + " = ?"
                                        , new String[]{String.valueOf(apiSeason.getIdentifiers().getTrakt())}
                                        , Contract.Episode.COLUMN_NUMBER + " ASC");

                        if (cursorEpisode != null) {
                            while (cursorEpisode.moveToNext()) {
                                ApiEpisode apiEpisode = new ApiEpisode(cursorEpisode);
                                apiSeason.getEpisodes().add(apiEpisode);
                            }
                            cursorEpisode.close();
                        }

                        apiShow.getSeasons().add(apiSeason);
                    }
                    cursorSeason.close();
                }
                apiShow.setNextEpisode(getNextEpisode(apiShow));
                apiShows.add(apiShow);
            }
            cursor.close();
        }


        return Observable.fromArray(apiShows);
    }

    @Override
    public ApiEpisode getNextEpisode(ApiShow apiShow) {
        ApiEpisode apiEpisode = null;
        Cursor cursor = App.getContext().getContentResolver()
                .query(Contract.Episode.
                                NEXTEPISODE_URI
                        , Contract.Episode.COLUMNS  // projection
                        , Contract.Season.COLUMN_SHOW_ID + " = ? AND " + Contract.Episode.COLUMN_WATCHED + " = 0 AND "
                                + Contract.Season.TABLE_NAME + "." + Contract.Season._ID + " = " + Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_SEASON_ID // selection
                        , new String[]{String.valueOf(apiShow.getTraktId())}
                        , Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_SEASON +
                                " ASC, " + Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_NUMBER + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            apiEpisode = new ApiEpisode(cursor);
            cursor.close();
        }
        return apiEpisode;
    }

    @Override
    public List<ApiEpisode> findEpisodesRelease() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 30);
        ArrayList<ApiEpisode> episodes = new ArrayList<>();

        Cursor cursor = App.getContext().getContentResolver()
                .query(Contract.Episode.URI_WITH_SHOW
                        , ObjectArrays.concat(Contract.Episode.COLUMNS,
                                new String[]{Contract.Show.TABLE_NAME + "." + Contract.Show.COLUMN_NAME, Contract.Show.TABLE_NAME + "." + Contract.Show.COLUMN_NETWORK}, String.class) // projection
                        , Contract.Episode.COLUMN_SEASON_ID + " = " + Contract.Season.TABLE_NAME + "." + Contract.Season._ID + " AND "
                                + Contract.Season.TABLE_NAME + "." + Contract.Season.COLUMN_SHOW_ID + " = " + Contract.Show.TABLE_NAME + "." + Contract.Show._ID + " AND "
                                + " date(" + Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_FIRST_AIRED + ") BETWEEN date('" + sdf.format(today.getTime()) + "') AND "
                                + " date('" + sdf.format(tomorrow.getTime()) + "')"
                        , new String[]{}
                        , "date(" + Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_FIRST_AIRED + ") ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ApiEpisode apiEpisode = new ApiEpisode(cursor);
                apiEpisode.setShowName(cursor.getString(cursor.getColumnIndex(Contract.Show.TABLE_NAME + "." + Contract.Show.COLUMN_NAME)));
                apiEpisode.setShowNetwork(cursor.getString(cursor.getColumnIndex(Contract.Show.TABLE_NAME + "." + Contract.Show.COLUMN_NETWORK)));
                episodes.add(apiEpisode);
            }

            cursor.close();
        }
        return episodes;
    }


    @Override
    public int updateEpisode(ApiEpisode apiEpisode) {
        return App.getContext().getContentResolver()
                .update(Contract.Episode.makeUriWithId(apiEpisode.getIdentifiers().getTrakt())
                        , apiEpisode.getContentValues()
                        , null
                        , new String[]{String.valueOf(apiEpisode.getIdentifiers().getTrakt())});
    }

    @Override
    public boolean isShowAdded(ApiMediaObject mediaObject) {
        Cursor cursor = App.getContext().getContentResolver().query(
                Contract.Show.makeUriWithId(String.valueOf(mediaObject.getApiIdentifiers().getTrakt()))
                , Contract.Show.SHOWS_COLUMNS, null
                , new String[]{String.valueOf(mediaObject.getApiIdentifiers().getTrakt())}
                , ""
        );
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    @Override
    public void insertShow(ApiShow apiShow) {
        App.getContext().getContentResolver()
                .insert(Contract.Show.URI, apiShow.getContentValues());
    }

    @Override
    public void insertSeason(ApiSeason apiSeason) {
        App.getContext().getContentResolver()
                .insert(Contract.Season.URI, apiSeason.getContentValues());
    }

    @Override
    public void insertEpisode(ApiEpisode apiEpisode) {
        App.getContext().getContentResolver()
                .insert(Contract.Episode.URI, apiEpisode.getContentValues());
    }

    @Override
    public int deleteShow(ApiShow apiShow) {
        for (ApiSeason season : apiShow.getSeasons()) {
            deleteSeason(season);
        }
        return App.getContext().getContentResolver()
                .delete(Contract.Show.URI
                        , Contract.Show._ID + " = ?"
                        , new String[]{String.valueOf(apiShow.getTraktId())});
    }

    @Override
    public int deleteSeason(ApiSeason apiSeason) {
        for (ApiEpisode episode : apiSeason.getEpisodes()) {
            deleteEpisode(episode);
        }

        return App.getContext().getContentResolver()
                .delete(Contract.Season.URI
                        , Contract.Season._ID + " = ?"
                        , new String[]{String.valueOf(apiSeason.getIdentifiers().getTrakt())});
    }

    @Override
    public int deleteEpisode(ApiEpisode apiEpisode) {
        return App.getContext().getContentResolver()
                .delete(Contract.Episode.URI
                        , Contract.Episode._ID + " = ?"
                        , new String[]{String.valueOf(apiEpisode.getIdentifiers().getTrakt())});
    }

}
