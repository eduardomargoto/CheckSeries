package br.com.etm.checkseries.api.mappers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.api.data.tracktv.ApiSearchObject;

/**
 * Created by eduardo on 06/01/18.
 */

public class SearchObjectMapper {

    private static final String MEDIA_SHOW = "show";
    private static final String MEDIA_MOVIE = "movie";

    @Inject
    public SearchObjectMapper() {}

    public List<ApiMediaObject> transform(List<ApiSearchObject> apiSearchObjects) {

        List<ApiMediaObject> apiMediaObjects = new ArrayList<>();
        for (ApiSearchObject apiSearchObject : apiSearchObjects) {
            ApiMediaObject apiMediaObject = new ApiMediaObject();
            switch (apiSearchObject.getType()) {
                case MEDIA_MOVIE:
                    apiMediaObject = apiSearchObject.getMovie();
                    break;
                case MEDIA_SHOW:
                    apiMediaObject = apiSearchObject.getShow();
                    break;
            }
            apiMediaObject.setType(apiSearchObject.getType());
            apiMediaObjects.add(apiMediaObject);
        }
        return apiMediaObjects;
    }
}
