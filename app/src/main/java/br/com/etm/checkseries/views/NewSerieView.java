package br.com.etm.checkseries.views;

import java.util.List;


import br.com.etm.checkseries.api.data.ApiSearchObject;

/**
 * Created by eduardo on 07/12/17.
 */

public interface NewSerieView {

    void configureView();
    void updateView(List<ApiSearchObject> apiSearchObjects);
    void showProgress();
    void dismissProgress();
}

