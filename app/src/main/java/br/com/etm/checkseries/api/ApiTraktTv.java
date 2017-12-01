package br.com.etm.checkseries.api;

import br.com.etm.checkseries.api.data.ApiResponseToken;
import br.com.etm.checkseries.api.data.ApiToken;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    ApiResponseToken revoke(@Header("Authorization") String access_token,
                            @Header("trakt-api-key") String client_id,
                            @Body ApiToken apiToken);

}
