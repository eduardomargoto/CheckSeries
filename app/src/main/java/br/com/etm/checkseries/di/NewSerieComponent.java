package br.com.etm.checkseries.di;

import br.com.etm.checkseries.activity.NewSerieActivity;
import br.com.etm.checkseries.scope.ActivityScope;
import dagger.Component;

/**
 * Created by eduardo on 07/12/17.
 */

@ActivityScope
@Component(
        dependencies = {AppComponent.class},
        modules = {NewSerieModule.class}
)
public interface NewSerieComponent {
    void inject(NewSerieActivity activity);
}
