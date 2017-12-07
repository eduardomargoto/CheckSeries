package br.com.etm.checkseries.api.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiEpisode extends ApiEpisodeObject {

    @SerializedName("overview")
    private String overview;
    @SerializedName("first_aired")
    private String dateFirstAired;
    @SerializedName("updated_at")
    private String dateUpdated;
    @SerializedName("rating")
    private Integer rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("runtime")
    private Integer runtime;

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

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
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

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}
