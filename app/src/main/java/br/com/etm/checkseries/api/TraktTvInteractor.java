package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by eduardo on 07/12/17.
 */

public interface TraktTvInteractor {

    Observable<List<ApiMediaObject>> search(String query);

    Observable<List<ApiMediaObject>> search(String type, String query);

    Observable<ApiShow> getShow(String showId);
}
