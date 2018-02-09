package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.trakTv.ApiAliases;
import br.com.etm.checkseries.api.data.trakTv.ApiMediaObject;
import br.com.etm.checkseries.api.data.trakTv.ApiSeason;
import br.com.etm.checkseries.api.data.trakTv.ApiShow;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public interface TraktTvInteractor {

    Observable<List<ApiMediaObject>> search(String query);

    Observable<List<ApiMediaObject>> search(String type, String query);

    Observable<ApiShow> getShow(String showId);

    Observable<List<ApiAliases>> getAliasesShow(String showId);

    Observable<List<ApiSeason>> getEpisodes(String showId);
}
