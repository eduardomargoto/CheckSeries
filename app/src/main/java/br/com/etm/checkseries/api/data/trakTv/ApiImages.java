package br.com.etm.checkseries.api.data.trakTv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiImages {

    @SerializedName("fanart")
    private ApiImagesObject fanart;
    @SerializedName("poster")
    private ApiImagesObject poster;
    @SerializedName("logo")
    private ApiImagesObject logo;
    @SerializedName("clearart")
    private ApiImagesObject clearart;
    @SerializedName("banner")
    private ApiImagesObject banner;
    @SerializedName("thumb")
    private ApiImagesObject thumb;

    public ApiImagesObject getFanart() {
        return fanart;
    }

    public void setFanart(ApiImagesObject fanart) {
        this.fanart = fanart;
    }

    public ApiImagesObject getPoster() {
        return poster;
    }

    public void setPoster(ApiImagesObject poster) {
        this.poster = poster;
    }

    public ApiImagesObject getLogo() {
        return logo;
    }

    public void setLogo(ApiImagesObject logo) {
        this.logo = logo;
    }

    public ApiImagesObject getClearart() {
        return clearart;
    }

    public void setClearart(ApiImagesObject clearart) {
        this.clearart = clearart;
    }

    public ApiImagesObject getBanner() {
        return banner;
    }

    public void setBanner(ApiImagesObject banner) {
        this.banner = banner;
    }

    public ApiImagesObject getThumb() {
        return thumb;
    }

    public void setThumb(ApiImagesObject thumb) {
        this.thumb = thumb;
    }
}
