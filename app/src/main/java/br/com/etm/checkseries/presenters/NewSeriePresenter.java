package br.com.etm.checkseries.presenters;

/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSeriePresenter {

    void onCreate();
    void searchSerie(String query);
    void onDestroy();
}
