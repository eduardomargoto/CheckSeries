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
                .doOnTerminate(view::dismissProgress)
                .subscribe(view::updateView, throwable -> {
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
}
