package br.com.etm.checkseries.presenters.impl;

import android.util.Log;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.views.SerieView;

/**
 * Created by eduardo on 08/01/18.
 */

public class SeriePresenterImpl implements SeriePresenter {

    private SerieView view;

    private DbInteractor dbInteractor;

    public SeriePresenterImpl(SerieView view, DbInteractor dbInteractor) {
        this.dbInteractor = dbInteractor;
        this.view = view;
    }

    @Override
    public void onCreate() {
        retrieveShows();
    }

    @Override
    public void retrieveShows() {
        view.configureView(dbInteractor.retrieveShows());
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
                dbInteractor.getNextEpisode(apiShow);
                view.notifyDataChanged(apiShow, position);
            } else {episode.setWatched(false);}
        } else {
            dbInteractor.getNextEpisode(apiShow);
        }
    }
}
