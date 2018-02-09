package br.com.etm.checkseries.views;

import java.util.List;


import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import br.com.etm.checkseries.api.data.trakTv.ApiMediaObject;

/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSerieView {

    void returnImage(int position,ApiFanArtObject apiFanArtObject);
    void configureView();
    void updateView(List<ApiMediaObject> apiMediaObjects);
    void showProgress();
    void dismissProgress();

    void onSerieAdded(int position);
}

