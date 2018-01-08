package br.com.etm.checkseries.presenters.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.App;
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
                        Contract.Show.SHOW_EPISODE_COLUMNS
                        , Contract.Show.TABLE_NAME + "." + Contract.Show._ID + " = " + Contract.Episode.TABLE_NAME + "." + Contract.Episode.COLUMN_SHOW_ID
                        , null
                        , null);

        List<ApiShow> apiShows = new ArrayList<>();
        if (cursor != null) {
            while(cursor.moveToNext()) {
                ApiShow apiShow = new ApiShow(cursor);
                apiShows.add(apiShow);
            }
        }

        view.configureView(apiShows);
    }


}
