package br.com.etm.checkseries.presenters.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.data.Contract;
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
    public void searchSerie(Context context, String query) {
        disposable = interactor.search(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .doOnTerminate(view::dismissProgress)
                .subscribe(apiMediaObjects -> {
                    for (ApiMediaObject mediaObject : apiMediaObjects) {
                        Cursor cursor = context.getContentResolver().query(
                                Contract.Show.makeUriWithId(String.valueOf(mediaObject.getApiIdentifiers().getTrakt()))
                                , Contract.Show.SHOWS_COLUMNS, null
                                , new String[]{String.valueOf(mediaObject.getApiIdentifiers().getTrakt())}
                                , ""
                        );
                        if (cursor != null && cursor.moveToFirst()) {
                            mediaObject.setAdded(true);
                        }
                        cursor.close();
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
    public void insert(Context context, int position, ApiMediaObject mediaObject) {
        interactor.getShow(String.valueOf(mediaObject.getApiIdentifiers().getTrakt()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiShow -> {
                    apiShow.setMediaObject(mediaObject);

                    Uri uri = context.getContentResolver()
                            .insert(Contract.Show.URI, apiShow.getContentValues());
                    if (uri != null) {view.onSerieAdded(position);}

                    insertNextEpisode(context, apiShow);
                }, throwable -> {
                    Log.e("Presenter", "insert - getShow", throwable);
                });
    }

    private void insertNextEpisode(Context context, ApiShow apiShow) {
        interactor.getEpisode(String.valueOf(apiShow.getTraktId()), 1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiEpisode -> {

                    apiEpisode.setShowTraktId(apiShow.getTraktId());
                    context.getContentResolver()
                            .insert(Contract.Episode.URI, apiEpisode.getContentValues());
                }, throwable -> {
                    Log.e("Presenter", "insert - getEpisode", throwable);
                });

    }


}
