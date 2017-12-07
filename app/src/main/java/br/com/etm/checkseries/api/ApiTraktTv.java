package br.com.etm.checkseries.api;

import java.util.List;

import br.com.etm.checkseries.api.data.ApiCheckInObject;
import br.com.etm.checkseries.api.data.ApiMovie;
import br.com.etm.checkseries.api.data.ApiResponseToken;
import br.com.etm.checkseries.api.data.ApiShow;
import br.com.etm.checkseries.api.data.ApiToken;
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


    @Headers({"Content-Type:application/x-www-form-urlencoded", "trakt-api-version:2"})
    @POST("/oauth/revoke")
    ApiResponseToken revoke(@Header("Authorization") String accessToken,
                            @Header("trakt-api-key") String clientId,
                            @Body ApiToken apiToken);

    @Headers({"Content-Type:application/x-www-form-urlencoded", "trakt-api-version:2"})
    @POST("/checkin")
    ApiShow checkIn(@Header("Authorization") String accessToken,
                    @Header("trakt-api-key") String clientId,
                    @Body ApiCheckInObject apiCheckinObject);



    @Headers({"Content-Type:application/x-www-form-urlencoded", "trakt-api-version:2"})
    @DELETE("/checkin")
    ApiShow deleteCheckIn(@Header("Authorization") String accessToken,
                    @Header("trakt-api-key") String clientId);

    @GET("/shows/{id}?extended=full")
    ApiShow getShow(@Path("id") String showId);

    @GET("/movies/{id}?extended=full")
    ApiMovie getMovie(@Path("id") String movieId);

    @GET("/search/{type}")
    ApiMovie search(@Path("type") List<String> types,
                    @Query("query") String searchText);
}
