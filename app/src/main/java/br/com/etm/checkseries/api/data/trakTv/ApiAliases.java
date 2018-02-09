package br.com.etm.checkseries.api.data.trakTv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 09/02/18.
 */

public class ApiAliases {

    @SerializedName("title")
    private String title;
    @SerializedName("country")
    private String country;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
