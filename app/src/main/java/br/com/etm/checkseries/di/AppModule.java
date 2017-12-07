package br.com.etm.checkseries.di;

import javax.inject.Singleton;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.TraktTvInteractorImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by eduardo on 07/12/17.
 */

@Module
public class AppModule {

    public AppModule() {
    }

    @Singleton
    @Provides
    TraktTvInteractor providesInteractor(){
        return new TraktTvInteractorImpl(App.getApi());
    }
}
