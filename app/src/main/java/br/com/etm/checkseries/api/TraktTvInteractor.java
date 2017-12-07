package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.ApiMediaObject;
import io.reactivex.Observable;

/**
 * Created by eduardo on 07/12/17.
 */

public interface TraktTvInteractor {

    Observable<List<ApiMediaObject>> search(String query);
}
