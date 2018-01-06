package br.com.etm.checkseries.presenters;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;

/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSeriePresenter {

    void onCreate();
    void searchSerie(String query);

    void retrieveImages(List<ApiMediaObject> apiMediaObjects);
    void onDestroy();
}
