package br.com.etm.checkseries.di.modules;

import javax.inject.Singleton;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.FanArtInteractorImpl;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.api.TraktTvInteractorImpl;
import br.com.etm.checkseries.api.mappers.SearchObjectMapper;
import dagger.Module;
import dagger.Provides;

/**
 * Created by eduardo on 07/12/17.
 */

@Module
public class AppModule {

    public AppModule() {
    }

    @Provides
    SearchObjectMapper providesSearchObjectMapper(){
        return new SearchObjectMapper();
    }


    @Provides
    TraktTvInteractor providesInteractor(SearchObjectMapper searchObjectMapper){
        return new TraktTvInteractorImpl(App.getApi(), searchObjectMapper);
    }

    @Provides
    FanArtInteractor providesFanArtInteractor(){
        return new FanArtInteractorImpl(App.getApiFanArt());
    }
}
