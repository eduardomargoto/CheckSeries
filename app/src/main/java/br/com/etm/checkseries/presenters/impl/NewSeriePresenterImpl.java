package br.com.etm.checkseries.presenters.impl;

import android.util.Log;

import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.views.NewSerieView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by eduardo on 07/12/17.
 */

public class NewSeriePresenterImpl implements NewSeriePresenter {

    private NewSerieView view;
    private TraktTvInteractor interactor;
    private Disposable disposable;

    public NewSeriePresenterImpl(NewSerieView view, TraktTvInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onCreate() {
        view.configureView();
    }

    @Override
    public void searchSerie(String query) {
        disposable = interactor.search(query)
                .subscribeOn(Schedulers.newThread())
                .subscribe(apiMediaObjects -> {
                    view.updateView(apiMediaObjects);
                }, throwable -> {
                    Log.e("Presenter", "searchSerie", throwable);
                });
    }

    @Override
    public void onDestroy() {
        if (disposable != null && disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
