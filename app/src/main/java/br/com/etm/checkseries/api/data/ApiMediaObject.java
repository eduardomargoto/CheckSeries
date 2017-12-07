package br.com.etm.checkseries.api.data;

import com.google.gson.annotations.SerializedName;


/**
 * Created by eduardo on 07/12/17.
 */

public class ApiMediaObject {

    @SerializedName("title")
    private String title;
    @SerializedName("year")
    private Integer year;
    @SerializedName("images")
    private ApiImages images;
    @SerializedName("ids")
    private ApiIdentifiers apiIdentifiers;

    public ApiImages getImages() {
        return images;
    }

    public void setImages(ApiImages images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public ApiIdentifiers getApiIdentifiers() {
        return apiIdentifiers;
    }

    public void setApiIdentifiers(ApiIdentifiers apiIdentifiers) {
        this.apiIdentifiers = apiIdentifiers;
    }
}
