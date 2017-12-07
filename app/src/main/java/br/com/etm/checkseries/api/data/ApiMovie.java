package br.com.etm.checkseries.api.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiMovie extends ApiMediaObject {

    @SerializedName("tagline")
    private String tagLine;
    @SerializedName("overview")
    private String overview;
    @SerializedName("released")
    private String date_released;
    @SerializedName("runtime")
    private Integer runtime;
    @SerializedName("updated_at")
    private String updated_time;
    @SerializedName("trailer")
    private String trailer;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("rating")
    private Integer rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("certification")
    private String cetification;
    @SerializedName("genres")
    private List<String> genres;

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDate_released() {
        return date_released;
    }

    public void setDate_released(String date_released) {
        this.date_released = date_released;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCetification() {
        return cetification;
    }

    public void setCetification(String cetification) {
        this.cetification = cetification;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
