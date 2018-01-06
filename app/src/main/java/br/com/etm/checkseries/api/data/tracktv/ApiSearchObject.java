package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiSearchObject {

    @SerializedName("type")
    private String type;
    @SerializedName("score")
    private Double score;
    @SerializedName("movie")
    private ApiMediaObject movie;
    @SerializedName("show")
    private ApiMediaObject show;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public ApiMediaObject getMovie() {
        return movie;
    }

    public void setMovie(ApiMediaObject movie) {
        this.movie = movie;
    }

    public ApiMediaObject getShow() {
        return show;
    }

    public void setShow(ApiMediaObject show) {
        this.show = show;
    }
}
