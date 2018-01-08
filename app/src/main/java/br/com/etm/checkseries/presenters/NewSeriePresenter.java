package br.com.etm.checkseries.presenters;


import android.content.Context;

import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;

/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSeriePresenter {

    void onCreate();
    void searchSerie(Context context, String query);

    void retrieveImages(int position, String id, String type);
    void onDestroy();

    void insert(Context context, int position, ApiMediaObject mediaObject);
}
