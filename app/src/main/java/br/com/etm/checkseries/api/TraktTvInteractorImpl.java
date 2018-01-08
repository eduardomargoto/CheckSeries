package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.api.mappers.SearchObjectMapper;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public class TraktTvInteractorImpl implements TraktTvInteractor {

    private static final String DEFAULT_TYPE_SEARCH = "movie,show";
    private static final String SERIE_TYPE_SEARCH = "show";
    private ApiTraktTv api;
    private SearchObjectMapper searchObjectMapper;

    public TraktTvInteractorImpl(ApiTraktTv api, SearchObjectMapper searchObjectMapper) {
        this.api = api;
        this.searchObjectMapper =searchObjectMapper;
    }

    @Override
    public Observable<List<ApiMediaObject>> search(String query) {
        return api.search(SERIE_TYPE_SEARCH, query)
                .map(searchObjectMapper::transform);
    }

    @Override
    public Observable<List<ApiMediaObject>> search(String type, String query) {
        if(type == null || type.isEmpty()){
            type = SERIE_TYPE_SEARCH;
        }
        return api.search(type, query)
                .map(searchObjectMapper::transform);
    }

    @Override
    public Observable<ApiShow> getShow(String showId) {
        return api.getShow(showId);
    }

    @Override
    public Observable<ApiEpisode> getEpisode(String showId, int season, int episodeNumber) {
        return api.getEpisode(showId, season, episodeNumber);
    }
}
