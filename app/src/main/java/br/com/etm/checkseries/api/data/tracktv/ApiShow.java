package br.com.etm.checkseries.api.data.tracktv;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.etm.checkseries.api.data.fanart.ApiFanArtImages;
import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import br.com.etm.checkseries.data.Contract;

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
    private String rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("genres")
    private List<String> genres;
    @SerializedName("aired_episodes")
    private Integer totalEpisodes;

    public ApiShow(Cursor cursor) {

        setApiIdentifiers(new ApiIdentifiers());
        getApiIdentifiers().setTrakt(cursor.getInt(Contract.Show.POSITION_ID));
        getApiIdentifiers().setTvdb(cursor.getInt(Contract.Show.POSITION_TVDB_ID));
        getApiIdentifiers().setImdb(cursor.getString(Contract.Show.POSITION_IMDB_ID));
        getApiIdentifiers().setTmdb(cursor.getInt(Contract.Show.POSITION_TMDB_ID));

        setTitle(cursor.getString(Contract.Show.POSITION_TITLE));
        setYear(cursor.getInt(Contract.Show.POSITION_YEAR));
        setType(cursor.getString(Contract.Show.POSITION_TYPE));

        setFanArtImages(new ApiFanArtObject());
        getFanArtImages().setHdTvLogoImages(new ArrayList<>());
        getFanArtImages().getHdTvLogoImages().add(new ApiFanArtImages(cursor.getString(Contract.Show.POSITION_LOGO_URL)));

        getFanArtImages().setSeasonBannerImages(new ArrayList<>());
        getFanArtImages().getSeasonBannerImages().add(new ApiFanArtImages(cursor.getString(Contract.Show.POSITION_BANNER_URL)));

        getFanArtImages().setTvPosterImages(new ArrayList<>());
        getFanArtImages().getTvPosterImages().add(new ApiFanArtImages(cursor.getString(Contract.Show.POSITION_POSTER_URL)));
        getFanArtImages().setSeasonFanArtImages(new ArrayList<>());
        getFanArtImages().getSeasonFanArtImages().add(new ApiFanArtImages(cursor.getString(Contract.Show.POSITION_FANART_URL)));

        overview = cursor.getString(Contract.Show.POSITION_OVERVIEW);
        dateFirstAired = cursor.getString(Contract.Show.POSITION_FIRST_AIRED);
        air = new ApiAirs();
        air.setDay(cursor.getString(Contract.Show.POSITION_AIR_DATE));
        air.setHour(cursor.getString(Contract.Show.POSITION_AIR_TIME));
        runtime = cursor.getInt(Contract.Show.POSITION_RUNTIME);
        network = cursor.getString(Contract.Show.POSITION_NETWORK);
        country = cursor.getString(Contract.Show.POSITION_COUNTRY);
        timeUpdated = cursor.getString(Contract.Show.POSITION_UPDATED_AT);
        trailer = cursor.getString(Contract.Show.POSITION_TRAILER);

        status = cursor.getString(Contract.Show.POSITION_STATUS);
        rating = cursor.getString(Contract.Show.POSITION_RATING);
        votes = cursor.getInt(Contract.Show.POSITION_VOTES);
        commentCount = cursor.getInt(Contract.Show.POSITION_COMMENT_COUNT);
        genres = Arrays.asList(cursor.getString(Contract.Show.POSITION_GENRES)
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(","));
        totalEpisodes = cursor.getInt(Contract.Show.POSITION_TOTAL_EPISODES);

    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Show._ID, getApiIdentifiers().getTrakt());
        contentValues.put(Contract.Show.COLUMN_NAME, getTitle());
        contentValues.put(Contract.Show.COLUMN_TVDB_ID, getApiIdentifiers().getTvdb());
        contentValues.put(Contract.Show.COLUMN_IMDB_ID, getApiIdentifiers().getImdb());
        contentValues.put(Contract.Show.COLUMN_TMDB_ID, getApiIdentifiers().getTmdb());
        contentValues.put(Contract.Show.COLUMN_YEAR, getYear());
        contentValues.put(Contract.Show.COLUMN_TYPE, getType());

        if(getFanArtImages() != null && getFanArtImages().getHdTvLogoImages() != null
                && !getFanArtImages().getHdTvLogoImages().isEmpty()) {
            contentValues.put(Contract.Show.COLUMN_LOGO_URL, getFanArtImages().getHdTvLogoImages().get(0).getUrl());
        } else {contentValues.put(Contract.Show.COLUMN_LOGO_URL, "");}
        if(getFanArtImages() != null && getFanArtImages().getSeasonBannerImages() != null
                && !getFanArtImages().getSeasonBannerImages().isEmpty()) {
            contentValues.put(Contract.Show.COLUMN_BANNER_URL, getFanArtImages().getSeasonBannerImages().get(0).getUrl());
        } else {contentValues.put(Contract.Show.COLUMN_BANNER_URL, "");}
        if(getFanArtImages() != null && getFanArtImages().getTvPosterImages() != null
                && !getFanArtImages().getTvPosterImages().isEmpty()) {
            contentValues.put(Contract.Show.COLUMN_POSTER_URL, getFanArtImages().getTvPosterImages().get(0).getUrl());
        } else {contentValues.put(Contract.Show.COLUMN_POSTER_URL, "");}
        if(getFanArtImages() != null && getFanArtImages().getSeasonFanArtImages() != null
                && !getFanArtImages().getSeasonFanArtImages().isEmpty()) {
            contentValues.put(Contract.Show.COLUMN_FANART_URL, getFanArtImages().getSeasonFanArtImages().get(0).getUrl());
        } else {contentValues.put(Contract.Show.COLUMN_FANART_URL, "");}

        contentValues.put(Contract.Show.COLUMN_OVERVIEW, overview);
        contentValues.put(Contract.Show.COLUMN_FIRST_AIRED, dateFirstAired);
        contentValues.put(Contract.Show.COLUMN_AIR_DATE, air.getDay());
        contentValues.put(Contract.Show.COLUMN_AIR_TIME, air.getHour());
        contentValues.put(Contract.Show.COLUMN_RUNTIME, runtime);
        contentValues.put(Contract.Show.COLUMN_NETWORK, network);
        contentValues.put(Contract.Show.COLUMN_COUNTRY, country);
        contentValues.put(Contract.Show.COLUMN_UPDATED_AT, timeUpdated);
        contentValues.put(Contract.Show.COLUMN_TRAILER, trailer);
        contentValues.put(Contract.Show.COLUMN_HOMEPAGE, homepage);
        contentValues.put(Contract.Show.COLUMN_STATUS, status);
        contentValues.put(Contract.Show.COLUMN_RATING, rating);
        contentValues.put(Contract.Show.COLUMN_VOTES, votes);
        contentValues.put(Contract.Show.COLUMN_COMMENT_COUNT, commentCount);
        contentValues.put(Contract.Show.COLUMN_GENRES, genres.toString());
        contentValues.put(Contract.Show.COLUMN_TOTAL_EPISODES, totalEpisodes);

        return contentValues;
    }
}

