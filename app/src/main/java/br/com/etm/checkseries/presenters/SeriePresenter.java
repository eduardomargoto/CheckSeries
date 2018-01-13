package br.com.etm.checkseries.presenters;

import br.com.etm.checkseries.api.data.tracktv.ApiShow;

/**
 * Created by eduardo on 08/01/18.
 */

public interface SeriePresenter {
    void onCreate();

    void retrieveShows();

    void updateShow(ApiShow apiShow);

    void nextEpisode(ApiShow apiShow, int position);

    void filter(String newText);

    void filter();

}
