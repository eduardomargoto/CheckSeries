package br.com.etm.checkseries.presenters.impl;

import android.util.Log;

import java.util.Arrays;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.data.trakTv.ApiAliases;
import br.com.etm.checkseries.api.data.trakTv.ApiShow;
import br.com.etm.checkseries.data.Contract;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.data.preferences.Preferences;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.views.SerieView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SeriePresenterImpl implements SeriePresenter {

    private SerieView view;

    private DbInteractor dbInteractor;
    private Preferences preferences;
    private TraktTvInteractor traktTvInteractor;

    public SeriePresenterImpl(SerieView view, TraktTvInteractor traktTvInteractor, DbInteractor dbInteractor, Preferences preferences) {
        this.dbInteractor = dbInteractor;
        this.view = view;
        this.preferences = preferences;
        this.traktTvInteractor = traktTvInteractor;
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
        if (apiShow.getNextEpisode() != null && position != -1) {
            apiShow.setWatchedNextEpisode(true);

            int updatesRow = dbInteractor.updateEpisode(apiShow.getNextEpisode());
            if (updatesRow >= 1) {
                apiShow.setNextEpisode(dbInteractor.getNextEpisode(apiShow));
                view.notifyDataChanged(apiShow, position);
            } else {
                apiShow.getNextEpisode().setWatched(false);
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

    @Override
    public void syncShowWithService(ApiShow apiShow) {
        traktTvInteractor.getShow(String.valueOf(apiShow.getTraktId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newApiShow -> {
                    dbInteractor.updateShow(newApiShow);
                }, throwable -> {
                    Log.e("Presenter", "syncShowWithService", throwable);
                });
    }

    @Override
    public void removeShow(ApiShow apiShow) {
        int rowsDeleted = dbInteractor.deleteShow(apiShow);
        Log.i("Presenter", "RowsDeleted:" + rowsDeleted);
    }

    @Override
    public void retrieveAliases(ApiShow apiShow) {
        traktTvInteractor.getAliasesShow(String.valueOf(apiShow.getTraktId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .doOnTerminate(view::dismissProgress)
                .subscribe(apiAliases -> {
                    String[] aliases = new String[apiAliases.size()];
                    for (int i = 0; i < apiAliases.size(); i++) {
                        aliases[i] = apiAliases.get(i).getTitle();
                    }
                    int checkItem = Arrays.asList(aliases).indexOf(apiShow.getTitle());
                    view.configureDialogAliases(aliases, checkItem, apiShow);
                }, throwable -> {
                    Log.e("Presenter", "syncShowWithService", throwable);
                });

    }

}
