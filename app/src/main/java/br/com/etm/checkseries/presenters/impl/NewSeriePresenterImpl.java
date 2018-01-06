package br.com.etm.checkseries.presenters.impl;

import android.util.Log;

import java.util.List;

import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.views.NewSerieView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by eduardo on 07/12/17.
 */

public class NewSeriePresenterImpl implements NewSeriePresenter {

    private static final String MEDIA_SHOW = "show";
    private static final String MEDIA_MOVIE = "movie";

    private NewSerieView view;
    private TraktTvInteractor interactor;
    private FanArtInteractor fanArtInteractor;
    private Disposable disposable;

    public NewSeriePresenterImpl(NewSerieView view, TraktTvInteractor interactor, FanArtInteractor fanArtInteractor) {
        this.view = view;
        this.interactor = interactor;
        this.fanArtInteractor = fanArtInteractor;
    }

    @Override
    public void onCreate() {
        view.configureView();
    }

    @Override
    public void searchSerie(String query) {
        disposable = interactor.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .subscribe(this::retrieveImages, throwable -> {
                    Log.e("Presenter", "searchSerie", throwable);
                });
    }

    @Override
    public void retrieveImages(List<ApiMediaObject> apiMediaObjects) {
        Observable.just(apiMediaObjects)
                .flatMapIterable(mediaObject -> apiMediaObjects)
                .doOnTerminate(() -> {
                    view.dismissProgress();
                    view.updateView(apiMediaObjects);
                })
                .subscribe(mediaObject -> {
                    String id = "";
                    switch (mediaObject.getType()) {
                        case MEDIA_SHOW:
                            id = String.valueOf(mediaObject.getApiIdentifiers().getTvdb());
                            break;
                        case MEDIA_MOVIE:
                            id = mediaObject.getApiIdentifiers().getImdb();
                            break;
                    }
                    fanArtInteractor.getImages(id, mediaObject.getType())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(mediaObject::setFanArtImages, throwable -> {
                                Log.e("Presenter", "retrieveImages", throwable);
                            });

                });
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
