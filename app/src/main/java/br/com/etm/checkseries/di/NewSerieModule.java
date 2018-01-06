package br.com.etm.checkseries.di;

import br.com.etm.checkseries.api.FanArtInteractor;
import br.com.etm.checkseries.api.TraktTvInteractor;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.presenters.impl.NewSeriePresenterImpl;
import br.com.etm.checkseries.views.NewSerieView;
import dagger.Module;
import dagger.Provides;

/**
 * Created by eduardo on 07/12/17.
 */

@Module
public class NewSerieModule {

    private NewSerieView view;

    public NewSerieModule(NewSerieView view) {
        this.view = view;
    }

    @Provides
    public NewSerieView providesView(){
        return this.view;
    }

    @Provides
    public NewSeriePresenter providesPresenter(NewSerieView view, TraktTvInteractor interactor, FanArtInteractor fanArtInteractor){
        return new NewSeriePresenterImpl(view, interactor, fanArtInteractor);
    }
}
