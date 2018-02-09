package br.com.etm.checkseries.api.data.trakTv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiImagesObject {

    @SerializedName("full")
    private String full;
    @SerializedName("medium")
    private String medium;
    @SerializedName("thumb")
    private String thumb;

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
