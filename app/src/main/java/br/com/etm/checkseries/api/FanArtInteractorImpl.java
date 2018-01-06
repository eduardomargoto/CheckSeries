package br.com.etm.checkseries.api;

import br.com.etm.checkseries.BuildConfig;
import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import io.reactivex.Observable;

/**
 * Created by eduardo on 06/01/18.
 */

public class FanArtInteractorImpl implements FanArtInteractor {

    private static final String MEDIA_SHOW = "show";
    private static final String MEDIA_MOVIE = "movie";

    private ApiFanArt api;

    public FanArtInteractorImpl(ApiFanArt api) {
        this.api = api;
    }

    @Override
    public Observable<ApiFanArtObject> getImages(String mediaObjectId, String typeMedia) {
        switch (typeMedia){
            case MEDIA_SHOW:
                return api.getImagesShow(mediaObjectId, BuildConfig.API_KEY_FANART);
            case MEDIA_MOVIE:
                return api.getImagesMovies(mediaObjectId, BuildConfig.API_KEY_FANART);
        }
        return null;
    }
}
