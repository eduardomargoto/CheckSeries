package br.com.etm.checkseries.api.data.fanart;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 06/01/18.
 */

public class ApiFanArtImages {
    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String url;

    @SerializedName("likes")
    private String likes;

    @SerializedName("season")
    private String season;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getLikes() {
        return likes;
    }

    public String getSeason() {
        return season;
    }
}
