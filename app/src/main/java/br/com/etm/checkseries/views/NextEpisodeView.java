package br.com.etm.checkseries.views;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;

/**
 * Created by eduardo on 08/02/18.
 */

public interface NextEpisodeView {

    void configureView(List<ApiEpisode> apiEpisodeList);
}
