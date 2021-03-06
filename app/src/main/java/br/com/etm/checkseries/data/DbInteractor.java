package br.com.etm.checkseries.data;



import java.util.List;

import br.com.etm.checkseries.api.data.trakTv.ApiEpisode;
import br.com.etm.checkseries.api.data.trakTv.ApiMediaObject;
import br.com.etm.checkseries.api.data.trakTv.ApiSeason;
import br.com.etm.checkseries.api.data.trakTv.ApiShow;
import io.reactivex.Observable;

/**
 * Created by eduardo on 11/01/18.
 */

public interface DbInteractor {

    int updateShow(ApiShow apiShow);

    Observable<List<ApiShow>> retrieveShows(String where, String order);

    ApiEpisode getNextEpisode(ApiShow apiShow);

    List<ApiEpisode> findEpisodesRelease();

    int updateEpisode(ApiEpisode apiEpisode);

    boolean isShowAdded(ApiMediaObject mediaObject);

    void insertShow(ApiShow apiShow);

    void insertSeason(ApiSeason apiSeason);

    void insertEpisode(ApiEpisode apiEpisode);

    int deleteShow(ApiShow apiShow);

    int deleteSeason(ApiSeason apiSeason);

    int deleteEpisode (ApiEpisode apiEpisode);
}
