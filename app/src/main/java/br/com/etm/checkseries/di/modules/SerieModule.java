package br.com.etm.checkseries.di.modules;

import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.data.preferences.Preferences;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.presenters.impl.SeriePresenterImpl;
import br.com.etm.checkseries.views.SerieView;
import dagger.Module;
import dagger.Provides;

/**
 * Created by eduardo on 08/01/18.
 */

@Module
public class SerieModule {

    private SerieView view;

    public SerieModule(SerieView view) {
        this.view = view;
    }

    @Provides
    public SerieView providesView() {
        return this.view;
    }

    @Provides
    public SeriePresenter providesPresenter(TraktTvInteractor traktTvInteractor, DbInteractor dbInteractor, Preferences preferences) {
        return new SeriePresenterImpl(view, traktTvInteractor, dbInteractor, preferences);
    }
}
