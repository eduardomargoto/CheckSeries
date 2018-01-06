package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiShow extends ApiMediaObject {

    @SerializedName("overview")
    private String overview;
    @SerializedName("first_aired")
    private String dateFirstAired;
    @SerializedName("airs")
    private ApiAirs air;
    @SerializedName("runtime")
    private Integer runtime;
    @SerializedName("network")
    private String network;
    @SerializedName("country")
    private String country;
    @SerializedName("updated_at")
    private String timeUpdated;
    @SerializedName("trailer")
    private String trailer;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("status")
    private String status;
    @SerializedName("rating")
    private Integer rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("genres")
    private List<String> genres;
    @SerializedName("aired_episodes")
    private Integer totalEpisodes;

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDateFirstAired() {
        return dateFirstAired;
    }

    public void setDateFirstAired(String dateFirstAired) {
        this.dateFirstAired = dateFirstAired;
    }

    public ApiAirs getAir() {
        return air;
    }

    public void setAir(ApiAirs air) {
        this.air = air;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(String timeUpdated) {
        this.timeUpdated = timeUpdated;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }
}

