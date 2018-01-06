package br.com.etm.checkseries.api;

import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eduardo on 06/01/18.
 */

public interface ApiFanArt {

    @GET("/movies/{id}")
    Observable<ApiFanArtObject> getImagesMovies(@Path("id") String movieId, @Query("api_key") String apiKeyFanArt);

    @GET("/tv/{id}")
    Observable<ApiFanArtObject> getImagesShow(@Path("id") String showId, @Query("api_key") String apiKeyFanArt);
}
