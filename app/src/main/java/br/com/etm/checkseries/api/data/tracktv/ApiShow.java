package br.com.etm.checkseries.api.data.tracktv;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import br.com.etm.checkseries.data.Contract;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiShow {

    private String title;
    private Integer year;
    private Integer traktId;
    private Integer tvdbId;
    private String imdbId;
    private Integer tmdbId;
    private String type;

    private boolean favourite;

    private String bannerUrl;
    private String posterUrl;
    private String backgroundUrl;

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

    private List<ApiSeason> seasons;

    private ApiEpisode nextEpisode = null;

    public ApiShow(Cursor cursor) {

        traktId = cursor.getInt(Contract.Show.POSITION_ID);
        tvdbId = cursor.getInt(Contract.Show.POSITION_TVDB_ID);
        imdbId = cursor.getString(Contract.Show.POSITION_IMDB_ID);
        tmdbId = cursor.getInt(Contract.Show.POSITION_TMDB_ID);

        title = cursor.getString(Contract.Show.POSITION_TITLE);
        year = cursor.getInt(Contract.Show.POSITION_YEAR);
        type = cursor.getString(Contract.Show.POSITION_TYPE);

        backgroundUrl = cursor.getString(Contract.Show.POSITION_BACKGROUND_URL);
        bannerUrl = cursor.getString(Contract.Show.POSITION_BANNER_URL);
        posterUrl = cursor.getString(Contract.Show.POSITION_POSTER_URL);

        favourite = cursor.getInt(Contract.Show.POSITION_FAVOURITE) == 1;

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

//        nextEpisode = new ApiEpisode(cursor);

    }

    public List<ApiSeason> getSeasons() {
        if (seasons == null) {
            seasons = new ArrayList<>();
        }
        return seasons;
    }

    public ApiEpisode getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(ApiEpisode nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public int getEpisodesWatched() {
        int totalWatched = 0;
        for(ApiSeason season : getSeasons()){
            totalWatched = totalWatched + season.getEpisodesWatched();
        }
        return totalWatched;
    }

    public void setSeasons(List<ApiSeason> seasons) {
        this.seasons = seasons;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
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

    public Integer getTraktId() {
        return traktId;
    }

    public void setTraktId(Integer traktId) {
        this.traktId = traktId;
    }

    public Integer getTvdbId() {
        return tvdbId;
    }

    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
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

    public void setMediaObject(ApiMediaObject mediaObject) {
        title = mediaObject.getTitle();
        type = mediaObject.getType();
        traktId = mediaObject.getApiIdentifiers().getTrakt();
        imdbId = mediaObject.getApiIdentifiers().getImdb();
        tvdbId = mediaObject.getApiIdentifiers().getTvdb();
        tmdbId = mediaObject.getApiIdentifiers().getTmdb();

        //TODO: get the best images, looking likes and season.
        backgroundUrl = mediaObject.getFanArtImages().getShowBackgroundImages().get(0).getUrl();
        bannerUrl = mediaObject.getFanArtImages().getTvBannerImages().get(0).getUrl();
        posterUrl = mediaObject.getFanArtImages().getTvPosterImages().get(0).getUrl();
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Show._ID, getTraktId());
        contentValues.put(Contract.Show.COLUMN_NAME, title);
        contentValues.put(Contract.Show.COLUMN_TVDB_ID, tvdbId);
        contentValues.put(Contract.Show.COLUMN_IMDB_ID, imdbId);
        contentValues.put(Contract.Show.COLUMN_TMDB_ID, tmdbId);
        contentValues.put(Contract.Show.COLUMN_YEAR, year);
        contentValues.put(Contract.Show.COLUMN_TYPE, type);

        contentValues.put(Contract.Show.COLUMN_BACKGROUND_URL, backgroundUrl);
        contentValues.put(Contract.Show.COLUMN_BANNER_URL, bannerUrl);
        contentValues.put(Contract.Show.COLUMN_POSTER_URL, posterUrl);

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

        contentValues.put(Contract.Show.COLUMN_FAVOURITE, favourite ? 1 : 0);

        return contentValues;
    }


}

