package br.com.etm.checkseries.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiSeason;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;

/**
 * Created by eduardo on 11/01/18.
 */

public class DbInteractorImpl implements DbInteractor {

    public DbInteractorImpl() {
    }

    @Override
    public int updateShow(ApiShow apiShow) {
        return  App.getContext().getContentResolver()
                .update(Contract.Show.URI
                        , apiShow.getContentValues()
                        , Contract.Show._ID + " = ?"
                        , new String[]{String.valueOf(apiShow.getTraktId())});
    }

    @Override
    public List<ApiShow> retrieveShows() {

        Cursor cursor = App.getContext().getContentResolver()
                .query(Contract.Show.URI,
                        Contract.Show.SHOWS_COLUMNS
                        , null
                        , null
                        , null);

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
                                        , Contract.Episode.COLUMN_NUMBER);

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

        return apiShows;
    }

    @Override
    public ApiEpisode getNextEpisode(ApiShow apiShow) {
        ApiEpisode apiEpisode = null;
        Cursor cursor = App.getContext().getContentResolver()
                .query(Contract.Episode.NEXTEPISODE_URI
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

}
