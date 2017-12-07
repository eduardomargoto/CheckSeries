package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.ApiSearchObject;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public class TraktTvInteractorImpl implements TraktTvInteractor {

    private static final String DEFAULT_TYPE_SEARCH = "movie,show";
    private ApiTraktTv api;

    public TraktTvInteractorImpl(ApiTraktTv api) {
        this.api = api;
    }

    @Override
    public Observable<List<ApiSearchObject>> search(String query) {
        return api.search(DEFAULT_TYPE_SEARCH, query);
    }

    @Override
    public Observable<List<ApiSearchObject>> search(String type, String query) {
        if(type == null || type.isEmpty()){
            type = DEFAULT_TYPE_SEARCH;
        }
        return api.search(type, query);
    }
}
