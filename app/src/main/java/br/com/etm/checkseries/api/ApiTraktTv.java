package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.BuildConfig;
import br.com.etm.checkseries.api.data.tracktv.ApiCheckInObject;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.api.data.tracktv.ApiMovie;
import br.com.etm.checkseries.api.data.tracktv.ApiPerson;
import br.com.etm.checkseries.api.data.tracktv.ApiResponseToken;
import br.com.etm.checkseries.api.data.tracktv.ApiSearchObject;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.api.data.tracktv.ApiToken;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by eduardo on 01/12/17.
 */

public interface ApiTraktTv {

    @GET("oauth/authorize")
    void authorize(@Query("response_type") String type,
                   @Query("client_id") String clientId,
                   @Query("redirect_uri") String uri);

    @POST("/oauth/token")
    ApiResponseToken getToken(@Body ApiToken apiToken);

    @Headers({"Content-Type:application/x-www-form-urlencoded", "trakt-api-version:2", "trakt-api-key: " + BuildConfig.CLIENT_ID})
    @POST("/oauth/revoke")
    ApiResponseToken revoke(@Header("Authorization") String accessToken,
                            @Body ApiToken apiToken);

    @Headers({"Content-Type:application/json", "trakt-api-version:2", "trakt-api-key: " + BuildConfig.CLIENT_ID})
    @POST("/checkin")
    ApiShow checkIn(@Header("Authorization") String accessToken,
                    @Body ApiCheckInObject apiCheckinObject);

    @Headers({"Content-Type:application/json", "trakt-api-version:2", "trakt-api-key: " + BuildConfig.CLIENT_ID})
    @DELETE("/checkin")
    ApiShow deleteCheckIn(@Header("Authorization") String accessToken);

    @GET("/shows/{id}?extended=full")
    Observable<ApiShow> getShow(@Path("id") String showId);

    @GET("/shows/{id}/seasons/{season}/episodes/{episode}")
    ApiEpisode getEpisode(@Path("id") String showId,
                          @Path("season") String seasonNumber,
                          @Path("episode") String episodeNumber);

    @Headers({"Content-Type:application/json", "trakt-api-version:2", "trakt-api-key: " + BuildConfig.CLIENT_ID})
    @GET("/people/{id}?extended=full")
    ApiPerson getPeople(@Path("id") String personId);

    @GET("/movies/{id}?extended=full,images")
    ApiMovie getMovie(@Path("id") String movieId);

    @Headers({"Content-Type:application/json",
            "trakt-api-version: 2",
            "trakt-api-key: " + BuildConfig.CLIENT_ID})
    @GET("/search/{type}?extended=full,images")
    Observable<List<ApiSearchObject>> search(@Path("type") String types,
                                             @Query("query") String searchText);
}
