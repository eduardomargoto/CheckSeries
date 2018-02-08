package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;
import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiMediaObject {

    private static final String MEDIA_SHOW = "show";
    private static final String MEDIA_MOVIE = "movie";

    @SerializedName("title")
    private String title;
    @SerializedName("year")
    private Integer year;
    @SerializedName("ids")
    private ApiIdentifiers apiIdentifiers;

    private boolean added;
    private String type;
    private ApiFanArtObject fanArtImages;

    public String getIdToImage() {
        switch (type) {
            case MEDIA_MOVIE:
                return apiIdentifiers.getImdb();
            case MEDIA_SHOW:
                return String.valueOf(apiIdentifiers.getTvdb());
        }
        return "";
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ApiFanArtObject getFanArtImages() {
        return fanArtImages;
    }

    public void setFanArtImages(ApiFanArtObject fanArtImages) {
        this.fanArtImages = fanArtImages;
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
