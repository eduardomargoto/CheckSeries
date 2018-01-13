package br.com.etm.checkseries.di.components;

import br.com.etm.checkseries.data.preferences.Preferences;

import javax.inject.Singleton;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.data.DbInteractor;
import br.com.etm.checkseries.di.modules.AppModule;
import dagger.Component;

/**
 * Created by eduardo on 07/12/17.
 */

@Singleton
@Component(
        modules = {AppModule.class}
)
public interface AppComponent {
    void inject(App app);

    TraktTvInteractor traktTvInteractor();

    FanArtInteractor fanArtInteractor();

    DbInteractor dbInteractor();

    Preferences preferences();
}
