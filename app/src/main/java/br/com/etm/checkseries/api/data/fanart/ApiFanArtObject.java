package br.com.etm.checkseries.api.data.fanart;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 06/01/18.
 */

public class ApiFanArtObject {

    @SerializedName("name")
    private String name;

    @SerializedName("thetvdb_id")
    private String thetvdb_id;

    @SerializedName("characterart")
    private List<ApiFanArtImages> charactersArtImages;

    @SerializedName("seasonposter")
    private List<ApiFanArtImages> seasonFanArtImages;

    @SerializedName("tvthumb")
    private List<ApiFanArtImages> tvThumbImages;

    @SerializedName("clearart")
    private List<ApiFanArtImages> clearArtImages;

    @SerializedName("clearlogo")
    private List<ApiFanArtImages> clearLogoImages;

    @SerializedName("hdtvlogo")
    private List<ApiFanArtImages> hdTvLogoImages;

    @SerializedName("hdclearart")
    private List<ApiFanArtImages> hdClearArtImages;

    @SerializedName("seasonthumb")
    private List<ApiFanArtImages> seasonThumbImages;

    @SerializedName("seasonbanner")
    private List<ApiFanArtImages> seasonBannerImages;

    @SerializedName("tvposter")
    private List<ApiFanArtImages> tvPosterImages;

    @SerializedName("tvbanner")
    private List<ApiFanArtImages> tvBannerImages;

    @SerializedName("showbackground")
    private List<ApiFanArtImages> showBackgroundImages;

    public String getName() {
        return name;
    }

    public String getThetvdb_id() {
        return thetvdb_id;
    }

    public List<ApiFanArtImages> getCharactersArtImages() {
        return charactersArtImages;
    }

    public List<ApiFanArtImages> getSeasonFanArtImages() {
        return seasonFanArtImages;
    }

    public List<ApiFanArtImages> getTvThumbImages() {
        return tvThumbImages;
    }

    public List<ApiFanArtImages> getClearArtImages() {
        return clearArtImages;
    }

    public List<ApiFanArtImages> getClearLogoImages() {
        return clearLogoImages;
    }

    public List<ApiFanArtImages> getHdTvLogoImages() {
        return hdTvLogoImages;
    }

    public List<ApiFanArtImages> getHdClearArtImages() {
        return hdClearArtImages;
    }

    public List<ApiFanArtImages> getSeasonThumbImages() {
        return seasonThumbImages;
    }

    public List<ApiFanArtImages> getSeasonBannerImages() {
        return seasonBannerImages;
    }

    public List<ApiFanArtImages> getTvPosterImages() {
        if(tvPosterImages == null)
            tvPosterImages = new ArrayList<>();
        return tvPosterImages;
    }

    public List<ApiFanArtImages> getTvBannerImages() {
        if(tvBannerImages == null)
            tvBannerImages = new ArrayList<>();
        return tvBannerImages;
    }

    public List<ApiFanArtImages> getShowBackgroundImages() {
        if (showBackgroundImages == null)
            return showBackgroundImages = new ArrayList<>();
        return showBackgroundImages;
    }

    public void setCharactersArtImages(List<ApiFanArtImages> charactersArtImages) {
        this.charactersArtImages = charactersArtImages;
    }

    public void setSeasonFanArtImages(List<ApiFanArtImages> seasonFanArtImages) {
        this.seasonFanArtImages = seasonFanArtImages;
    }

    public void setTvThumbImages(List<ApiFanArtImages> tvThumbImages) {
        this.tvThumbImages = tvThumbImages;
    }

    public void setClearArtImages(List<ApiFanArtImages> clearArtImages) {
        this.clearArtImages = clearArtImages;
    }

    public void setClearLogoImages(List<ApiFanArtImages> clearLogoImages) {
        this.clearLogoImages = clearLogoImages;
    }

    public void setHdTvLogoImages(List<ApiFanArtImages> hdTvLogoImages) {
        this.hdTvLogoImages = hdTvLogoImages;
    }

    public void setHdClearArtImages(List<ApiFanArtImages> hdClearArtImages) {
        this.hdClearArtImages = hdClearArtImages;
    }

    public void setSeasonThumbImages(List<ApiFanArtImages> seasonThumbImages) {
        this.seasonThumbImages = seasonThumbImages;
    }

    public void setSeasonBannerImages(List<ApiFanArtImages> seasonBannerImages) {
        this.seasonBannerImages = seasonBannerImages;
    }

    public void setTvPosterImages(List<ApiFanArtImages> tvPosterImages) {
        this.tvPosterImages = tvPosterImages;
    }

    public void setTvBannerImages(List<ApiFanArtImages> tvBannerImages) {
        this.tvBannerImages = tvBannerImages;
    }

    public void setShowBackgroundImages(List<ApiFanArtImages> showBackgroundImages) {
        this.showBackgroundImages = showBackgroundImages;
    }
}
