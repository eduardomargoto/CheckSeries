package br.com.etm.checkseries.di.components;

import br.com.etm.checkseries.di.modules.NewSerieModule;
import br.com.etm.checkseries.fragments.NewSerieFragment;
import br.com.etm.checkseries.scopes.FragmentScope;
import dagger.Component;

/**
 * Created by eduardo on 07/12/17.
 */

@FragmentScope
@Component(
        dependencies = {AppComponent.class},
        modules = {NewSerieModule.class}
)
public interface NewSerieComponent {
    void inject(NewSerieFragment fragment);
}
