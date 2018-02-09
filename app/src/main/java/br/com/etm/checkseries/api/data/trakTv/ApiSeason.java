package br.com.etm.checkseries.api.data.trakTv;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.data.Contract;

/**
 * Created by eduardo on 10/01/18.
 */

public class ApiSeason {

    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("first_aired")
    private String firstAired;
    @SerializedName("number")
    private Integer number;
    @SerializedName("ids")
    private ApiIdentifiers identifiers;
    @SerializedName("rating")
    private Double rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("episode_count")
    private Integer episodeCount;
    @SerializedName("aired_episodes")
    private Integer airedEpisodes;

    @SerializedName("episodes")
    private List<ApiEpisode> episodes;

    private Integer showTraktId;

    public int getEpisodesWatched() {
        int totalWatched = 0;
        for(ApiEpisode episode: getEpisodes()){
            if(episode.isWatched()){
                totalWatched++;
            }
        }
        return totalWatched;
    }

    public Integer getShowTraktId() {
        return showTraktId;
    }

    public void setShowTraktId(Integer showTraktId) {
        this.showTraktId = showTraktId;
    }

    public ApiSeason(Cursor cursor) {

        setIdentifiers(new ApiIdentifiers());
        identifiers.setTrakt(cursor.getInt(Contract.Season.POSITION_ID));
        identifiers.setTvdb(cursor.getInt(Contract.Season.POSITION_TVDB_ID));
        identifiers.setImdb(cursor.getString(Contract.Season.POSITION_IMDB_ID));
        identifiers.setTmdb(cursor.getInt(Contract.Season.POSITION_TMDB_ID));
        title = cursor.getString(Contract.Season.POSITION_TITLE);
        number = cursor.getInt(Contract.Season.POSITION_NUMBER);
        overview = cursor.getString(Contract.Season.POSITION_OVERVIEW);
        firstAired = cursor.getString(Contract.Season.POSITION_FIRST_AIRED);
        rating = cursor.getDouble(Contract.Season.POSITION_RATING);
        votes = cursor.getInt(Contract.Season.POSITION_VOTES);
        episodeCount = cursor.getInt(Contract.Season.POSITION_EPISODE_COUNT);
        airedEpisodes = cursor.getInt(Contract.Season.POSITION_AIRED_EPISODES);


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ApiIdentifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ApiIdentifiers identifiers) {
        this.identifiers = identifiers;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getAiredEpisodes() {
        return airedEpisodes;
    }

    public void setAiredEpisodes(Integer airedEpisodes) {
        this.airedEpisodes = airedEpisodes;
    }

    public List<ApiEpisode> getEpisodes() {
        if (episodes == null)
            episodes = new ArrayList<>();
        return episodes;
    }

    public void setEpisodes(List<ApiEpisode> episodes) {
        this.episodes = episodes;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Season._ID, getIdentifiers().getTrakt());
        contentValues.put(Contract.Season.COLUMN_TITLE, title);
        contentValues.put(Contract.Season.COLUMN_TVDB_ID, getIdentifiers().getTvdb());
        contentValues.put(Contract.Season.COLUMN_IMDB_ID, getIdentifiers().getImdb());
        contentValues.put(Contract.Season.COLUMN_TMDB_ID, getIdentifiers().getTmdb());
        contentValues.put(Contract.Season.COLUMN_NUMBER, number);
        contentValues.put(Contract.Season.COLUMN_OVERVIEW, overview);
        contentValues.put(Contract.Season.COLUMN_FIRST_AIRED, firstAired);
        contentValues.put(Contract.Season.COLUMN_RATING, rating);
        contentValues.put(Contract.Season.COLUMN_VOTES, votes);

        contentValues.put(Contract.Season.COLUMN_EPISODE_COUNT, votes);
        contentValues.put(Contract.Season.COLUMN_AIRED_EPISODE, votes);

        contentValues.put(Contract.Season.COLUMN_SHOW_ID, showTraktId);

        return contentValues;
    }

}
