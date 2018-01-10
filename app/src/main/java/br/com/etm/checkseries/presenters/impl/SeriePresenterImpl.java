package br.com.etm.checkseries.presenters.impl;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiSeason;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.data.Contract;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.views.SerieView;

/**
 * Created by eduardo on 08/01/18.
 */

public class SeriePresenterImpl implements SeriePresenter {

    private SerieView view;

    public SeriePresenterImpl(SerieView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        retrieveShows();
    }

    @Override
    public void retrieveShows() {
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
                apiShows.add(apiShow);
            }
            cursor.close();
        }

        view.configureView(apiShows);
    }

    @Override
    public void updateShow(ApiShow apiShow) {
        int updateCount = App.getContext().getContentResolver()
                .update(Contract.Show.URI
                        , apiShow.getContentValues()
                        , Contract.Show._ID + " = ?"
                        , new String[]{String.valueOf(apiShow.getTraktId())});

        Log.i("Presenter", "updateCount:" + updateCount);

    }

    @Override
    public void nextEpisode(ApiShow apiShow) {

    }
}
