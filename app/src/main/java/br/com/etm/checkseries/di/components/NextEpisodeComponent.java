package br.com.etm.checkseries.di.components;

import br.com.etm.checkseries.di.modules.NextEpisodeModule;
import br.com.etm.checkseries.fragments.NextEpisodeFragment;
import br.com.etm.checkseries.scopes.FragmentScope;
import dagger.Component;

/**
 * Created by eduardo on 08/02/18.
 */

@FragmentScope
@Component(
        dependencies = {AppComponent.class},
        modules = {NextEpisodeModule.class}
)
public interface NextEpisodeComponent {
    void inject(NextEpisodeFragment fragment);
}
