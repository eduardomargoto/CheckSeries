package br.com.etm.checkseries.presenters.impl;

import java.util.List;
import br.com.etm.checkseries.api.data.trakTv.ApiEpisode;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.presenters.NextEpisodePresenter;
import br.com.etm.checkseries.views.NextEpisodeView;

public class NextEpisodePresenterImpl implements NextEpisodePresenter {

    private NextEpisodeView view;
    private DbInteractor dbInteractor;

    public NextEpisodePresenterImpl(NextEpisodeView view, DbInteractor dbInteractor) {
        this.view = view;
        this.dbInteractor = dbInteractor;
    }

    @Override
    public void onCreate() {
        List<ApiEpisode> apiEpisodes = dbInteractor.findEpisodesRelease();
        view.configureView(apiEpisodes);
    }
}
