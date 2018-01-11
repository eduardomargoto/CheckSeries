package br.com.etm.checkseries.presenters.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiSeason;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.data.Contract;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.views.NewSerieView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by eduardo on 07/12/17.
 */

public class NewSeriePresenterImpl implements NewSeriePresenter {

    private NewSerieView view;
    private TraktTvInteractor interactor;
    private FanArtInteractor fanArtInteractor;
    private DbInteractor dbInteractor;
    private Disposable disposable;

    public NewSeriePresenterImpl(NewSerieView view, TraktTvInteractor interactor, FanArtInteractor fanArtInteractor, DbInteractor dbInteractor) {
        this.view = view;
        this.interactor = interactor;
        this.fanArtInteractor = fanArtInteractor;
        this.dbInteractor = dbInteractor;
    }

    @Override
    public void onCreate() {
        view.configureView();
    }

    @Override
    public void searchSerie(Context context, String query) {
        disposable = interactor.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .doOnTerminate(view::dismissProgress)
                .subscribe(apiMediaObjects -> {
                    for (ApiMediaObject mediaObject : apiMediaObjects) {
                        mediaObject.setAdded(dbInteractor.isShowAdded(mediaObject));
                    }
                    view.updateView(apiMediaObjects);
                }, throwable -> {
                    Log.e("Presenter", "searchSerie", throwable);
                });
    }

    @Override
    public void retrieveImages(int position, String id, String type) {
        fanArtInteractor.getImages(id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiFanArtObject -> {
                    view.returnImage(position, apiFanArtObject);
                }, throwable -> {
                    Log.e("Presenter", "retrieveImages", throwable);
                });
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void insert(int position, ApiMediaObject mediaObject) {
        interactor.getShow(String.valueOf(mediaObject.getApiIdentifiers().getTrakt()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> view.onSerieAdded(position))
                .subscribe(apiShow -> {
                    apiShow.setMediaObject(mediaObject);
                    dbInteractor.insertShow(apiShow);
                    insertNextEpisode(apiShow);
                }, throwable -> {
                    Log.e("Presenter", "insert - getShow", throwable);
                });
    }

    private void insertNextEpisode(ApiShow apiShow) {
        interactor.getEpisodes(String.valueOf(apiShow.getTraktId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiSeasons -> {
                    apiShow.setSeasons(apiSeasons);
                    for(ApiSeason apiSeason : apiSeasons){
                        apiSeason.setShowTraktId(apiShow.getTraktId());
                        dbInteractor.insertSeason(apiSeason);

                        for(ApiEpisode apiEpisode : apiSeason.getEpisodes()) {
                            apiEpisode.setSeasonTraktId(apiSeason.getIdentifiers().getTrakt());

                            dbInteractor.insertEpisode(apiEpisode);
                        }
                    }

                }, throwable -> {
                    Log.e("Presenter", "insert - getEpisode", throwable);
                });

    }


}
