package br.com.etm.checkseries.api.data.trakTv;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eduardo on 01/12/17.
 */

public class ApiResponseToken implements Serializable {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private long expiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("scope")
    private String scope;
    @SerializedName("created_at")
    private long createdAt;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
