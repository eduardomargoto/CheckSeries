package br.com.etm.checkseries.api.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiEpisodeObject {

    @SerializedName("ids")
    private ApiIdentifiers identifiers;
    @SerializedName("season")
    private Integer season;
    @SerializedName("number")
    private Integer number;
    @SerializedName("title")
    private String title;

    public ApiIdentifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ApiIdentifiers identifiers) {
        this.identifiers = identifiers;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
