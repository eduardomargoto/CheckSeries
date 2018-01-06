package br.com.etm.checkseries.api;

import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import io.reactivex.Observable;

/**
 * Created by eduardo on 06/01/18.
 */

public interface FanArtInteractor {

    Observable<ApiFanArtObject> getImages(String mediaObjectId,String typeMedia);
}
