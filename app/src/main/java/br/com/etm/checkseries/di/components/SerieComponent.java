package br.com.etm.checkseries.di.components;

import br.com.etm.checkseries.di.modules.SerieModule;
import br.com.etm.checkseries.fragments.SerieFragment;
import dagger.Component;

/**
 * Created by eduardo on 08/01/18.
 */

@Component(
        dependencies = {AppComponent.class},
        modules = {SerieModule.class}
)
public interface SerieComponent {
    void inject(SerieFragment fragment);
}
