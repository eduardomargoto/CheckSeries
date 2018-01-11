package br.com.etm.checkseries.data;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiSeason;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;

/**
 * Created by eduardo on 11/01/18.
 */

public interface DbInteractor {

    int updateShow(ApiShow apiShow);

    List<ApiShow> retrieveShows();

    ApiEpisode getNextEpisode(ApiShow apiShow);

    int updateEpisode(ApiEpisode apiEpisode);

    boolean isShowAdded(ApiMediaObject mediaObject);

    void insertShow(ApiShow apiShow);

    void insertSeason(ApiSeason apiSeason);

    void insertEpisode(ApiEpisode apiEpisode);
}
