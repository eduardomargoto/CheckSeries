package br.com.etm.checkseries.presenters;


/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSeriePresenter {

    void onCreate();
    void searchSerie(String query);

    void retrieveImages(int position, String id, String type);
    void onDestroy();
}
