package br.com.etm.checkseries.api.data.tracktv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.data.Contract;

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
    private String rating;
    @SerializedName("votes")
    private Integer votes;
    @SerializedName("comment_count")
    private Integer commentCount;
    @SerializedName("runtime")
    private Integer runtime;
    @SerializedName("number_abs")
    private Integer numberAbs;
    @SerializedName("available_translations")
    private List<String> availableTranslations;

    private boolean watched;
    private Integer seasonTraktId;
    private String backgroundUrl;

    private String showName;
    private String showNetwork;

    public ApiEpisode(Cursor cursor) {

        setIdentifiers(new ApiIdentifiers());
        getIdentifiers().setTrakt(cursor.getInt(cursor.getColumnIndex(Contract.Episode._ID)));
        getIdentifiers().setTvdb(cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_TVDB_ID)));
        getIdentifiers().setTmdb(cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_TMDB_ID)));
        getIdentifiers().setImdb(cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_IMDB_ID)));

        setTitle(cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_TITLE)));
        setSeason(cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_SEASON)));
        setNumber(cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_NUMBER)));
        numberAbs = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_NUMBER_ABS));

        backgroundUrl = cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_BACKGROUND_URL));
        overview = cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_OVERVIEW));
        dateFirstAired = cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_FIRST_AIRED));

        runtime = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_RUNTIME));
        dateUpdated = cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_UPDATED_AT));
        rating = cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_RATING));
        votes = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_VOTES));
        commentCount = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_COMMENT_COUNT));
        availableTranslations = Arrays.asList(cursor.getString(cursor.getColumnIndex(Contract.Episode.COLUMN_AVAILABLE_TRANSLATIONS))
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(","));

        seasonTraktId = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_SEASON_ID));

        watched = cursor.getInt(cursor.getColumnIndex(Contract.Episode.COLUMN_WATCHED)) == 1;
    }

    public String getShowNetwork() {
        return showNetwork;
    }

    public void setShowNetwork(String showNetwork) {
        this.showNetwork = showNetwork;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String show_name) {
        this.showName = show_name;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public Integer getSeasonTraktId() {
        return seasonTraktId;
    }

    public void setSeasonTraktId(Integer seasonTraktId) {
        this.seasonTraktId = seasonTraktId;
    }

    public List<String> getAvailableTranslations() {
        return availableTranslations;
    }

    public void setAvailableTranslations(List<String> availableTranslations) {
        this.availableTranslations = availableTranslations;
    }

    public Integer getNumberAbs() {
        return numberAbs;
    }

    public void setNumberAbs(Integer numberAbs) {
        this.numberAbs = numberAbs;
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

    public String getDateFirstAiredFormatted(Context context) {
        if (this.dateFirstAired != null) {
            DateFormat dfmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
                Date date = formatter.parse(this.dateFirstAired.replace("Z", "").replace("T", " "));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dfmt.format(date) + " " + sdf.format(date);
            } catch (ParseException e) {
                Log.e("ParseException", "dateFirstAired", e);
                return context.getResources().getString(R.string.app_text_date_unknown);
            }
        }
        return context.getResources().getString(R.string.app_text_date_unknown);
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

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiEpisode apiEpisode = (ApiEpisode) o;

        if (getIdentifiers().getTrakt() != null ? !getIdentifiers().getTrakt().equals(apiEpisode.getIdentifiers().getTrakt()) : apiEpisode.getIdentifiers().getTrakt() != null)
            return false;

        return true;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Episode._ID, getIdentifiers().getTrakt());
        contentValues.put(Contract.Episode.COLUMN_TITLE, getTitle());
        contentValues.put(Contract.Episode.COLUMN_TVDB_ID, getIdentifiers().getTvdb());
        contentValues.put(Contract.Episode.COLUMN_IMDB_ID, getIdentifiers().getImdb());
        contentValues.put(Contract.Episode.COLUMN_TMDB_ID, getIdentifiers().getTmdb());
        contentValues.put(Contract.Episode.COLUMN_SEASON, getSeason());
        contentValues.put(Contract.Episode.COLUMN_NUMBER, getNumber());
        contentValues.put(Contract.Episode.COLUMN_NUMBER_ABS, numberAbs);
        contentValues.put(Contract.Episode.COLUMN_BACKGROUND_URL, backgroundUrl);

        contentValues.put(Contract.Episode.COLUMN_OVERVIEW, overview);
        contentValues.put(Contract.Episode.COLUMN_FIRST_AIRED, dateFirstAired);

        contentValues.put(Contract.Episode.COLUMN_RUNTIME, runtime);
        contentValues.put(Contract.Episode.COLUMN_UPDATED_AT, dateUpdated);
        contentValues.put(Contract.Episode.COLUMN_RATING, rating);
        contentValues.put(Contract.Episode.COLUMN_VOTES, votes);
        contentValues.put(Contract.Episode.COLUMN_COMMENT_COUNT, commentCount);
        contentValues.put(Contract.Episode.COLUMN_AVAILABLE_TRANSLATIONS, availableTranslations.toString());
        contentValues.put(Contract.Episode.COLUMN_WATCHED, watched ? 1 : 0);
        contentValues.put(Contract.Episode.COLUMN_SEASON_ID, seasonTraktId);

        return contentValues;
    }
}
