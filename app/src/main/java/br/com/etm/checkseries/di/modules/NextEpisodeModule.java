package br.com.etm.checkseries.di.modules;

import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.presenters.NextEpisodePresenter;
import br.com.etm.checkseries.presenters.impl.NextEpisodePresenterImpl;
import br.com.etm.checkseries.views.NextEpisodeView;
import dagger.Module;
import dagger.Provides;

/**
 * Created by eduardo on 08/02/18.
 */

@Module
public class NextEpisodeModule {
    private NextEpisodeView view;

    public NextEpisodeModule(NextEpisodeView view) {
        this.view = view;
    }

    @Provides
    public NextEpisodeView providesView() {
        return view;
    }

    @Provides
    public NextEpisodePresenter providesPresenter(DbInteractor dbInteractor) {
        return new NextEpisodePresenterImpl(view, dbInteractor);
    }
}
