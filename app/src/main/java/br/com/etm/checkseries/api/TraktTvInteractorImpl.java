package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.mappers.SearchObjectMapper;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public class TraktTvInteractorImpl implements TraktTvInteractor {

    private static final String DEFAULT_TYPE_SEARCH = "movie,show";
    private ApiTraktTv api;
    private SearchObjectMapper searchObjectMapper;

    public TraktTvInteractorImpl(ApiTraktTv api, SearchObjectMapper searchObjectMapper) {
        this.api = api;
        this.searchObjectMapper =searchObjectMapper;
    }

    @Override
    public Observable<List<ApiMediaObject>> search(String query) {
        return api.search(DEFAULT_TYPE_SEARCH, query)
                .map(searchObjectMapper::transform);
    }

    @Override
    public Observable<List<ApiMediaObject>> search(String type, String query) {
        if(type == null || type.isEmpty()){
            type = DEFAULT_TYPE_SEARCH;
        }
        return api.search(type, query)
                .map(searchObjectMapper::transform);
    }
}
