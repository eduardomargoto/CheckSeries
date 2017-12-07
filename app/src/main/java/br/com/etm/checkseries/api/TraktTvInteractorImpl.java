package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.ApiMediaObject;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public class TraktTvInteractorImpl implements TraktTvInteractor {

    private ApiTraktTv api;

    public TraktTvInteractorImpl(ApiTraktTv api) {
        this.api = api;
    }

    @Override
    public Observable<List<ApiMediaObject>> search(String query) {
        String types = "movie, show";
        return api.search(types, query);
    }
}
