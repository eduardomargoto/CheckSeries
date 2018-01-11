package br.com.etm.checkseries.views;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiShow;

/**
 * Created by eduardo on 08/01/18.
 */

public interface SerieView {

    void configureView(List<ApiShow> apiShows);

    void notifyDataChanged(ApiShow apiShow, int position);
}
