package br.com.etm.checkseries.presenters.impl;

import android.util.Log;

import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.data.Contract;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.data.preferences.Preferences;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.views.SerieView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by eduardo on 08/01/18.
 */

public class SeriePresenterImpl implements SeriePresenter {

    private SerieView view;

    private DbInteractor dbInteractor;
    private Preferences preferences;

    public SeriePresenterImpl(SerieView view, DbInteractor dbInteractor, Preferences preferences) {
        this.dbInteractor = dbInteractor;
        this.view = view;
        this.preferences = preferences;
    }

    @Override
    public void onCreate() {
        retrieveShows();
    }

    @Override
    public void retrieveShows() {
        dbInteractor.retrieveShows(Contract.Show.makeWhereFilter(preferences), Contract.Show.makeOrder(preferences))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiShows -> {
                    view.configureView(apiShows);
                });
    }

    @Override
    public void updateShow(ApiShow apiShow) {
        int updateCount = dbInteractor.updateShow(apiShow);
        Log.i("Presenter", "updateCount:" + updateCount);
    }

    @Override
    public void nextEpisode(ApiShow apiShow, int position) {
        ApiEpisode episode = apiShow.getNextEpisode();
        if (episode != null && position != -1) {
            episode.setWatched(true);

            int updatesRow = dbInteractor.updateEpisode(episode);
            if (updatesRow >= 1) {
                apiShow.setNextEpisode(dbInteractor.getNextEpisode(apiShow));
                view.notifyDataChanged(apiShow, position);
            } else {
                episode.setWatched(false);
            }
        } else {
            apiShow.setNextEpisode(dbInteractor.getNextEpisode(apiShow));
        }
    }

    @Override
    public void filter(String newText) {
        dbInteractor.retrieveShows(Contract.Show.COLUMN_NAME + " LIKE '%" + newText + "%'", Contract.Show.makeOrder(preferences))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiShows -> {
                    view.updateRecyclerView(apiShows);
                });
    }

    @Override
    public void filter() {
        dbInteractor.retrieveShows(Contract.Show.makeWhereFilter(preferences), Contract.Show.makeOrder(preferences))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiShows -> {
                    view.updateRecyclerView(apiShows);
                });
    }

}
