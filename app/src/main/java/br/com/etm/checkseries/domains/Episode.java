package br.com.etm.checkseries.domains;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class Episode implements Serializable {

/* <DVD_chapter />
    <DVD_discid />
    <DVD_episodenumber />
    <DVD_season />*/
//    <GuestStars>Lindsay Duncan|Peter O'Brien|Sharon Duncan Brewster</GuestStars>
    /*<ProductionCode />
    <Rating>8.0</Rating>
    <RatingCount>43</RatingCount>*/

    private int id; //<id>533011</id>
    private int episodeNumber; // <EpisodeNumber>14</EpisodeNumber>
    private int totalEpisodeNumber;
    private int seasonNumber; //<SeasonNumber>0</SeasonNumber>
    private String director; // <Director>Graeme Harper</Director>
    private String epImgFlag; //<EpImgFlag>2</EpImgFlag>
    private String episodeName; // <EpisodeName>The Waters of Mars</EpisodeName>
    private Date firstAired; //<FirstAired>2009-11-15</FirstAired>
    private String imdb_id; // <IMDB_ID />
    private String language; // <Language>en</Language>
    private String overview; // <Overview>Location: Bowie Base One, Mars Date: 21st November 2059 Enemies: The Flood The Doctor...</Overview>
    private String writer; //<Writer>Russell T Davies|Phil Ford</Writer>
    private String airAfterSeason; // <airsafter_season>4</airsafter_season>
    private String airBeforeEpisode; //<airsbefore_episode />
    private String airBeforeSeason; //<airsbefore_season />
    private String filename;  //<filename>episodes/78804/533011.jpg</filename>
    private Bitmap bm_poster;  //<filename>episodes/78804/533011.jpg</filename>
    private String lastUpdated; // <lastupdated>1378146306</lastupdated>
    private Integer serieId; //<seriesid>78804</seriesid>
    private Date thumbAdded; //<thumb_added>2013-11-22 11:17:43</thumb_added>
    private String thumbHight; //<thumb_height>225</thumb_height>
    private String thumbWidth; //<thumb_width>400</thumb_width>
    private String seasonId; //<seasonid>26260</seasonid>
    private Date dateWatched;

    private boolean watched = false;
    private boolean skipped = false;

    public Episode() {
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String getFirstAiredFormatted(Context context) {
        if (this.firstAired != null) {
            DateFormat dfmt = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            return dfmt.format(this.firstAired);
        }
        return context.getResources().getString(R.string.app_text_date_unknown);
    }

    public String getDateEpisodeFormatted(Context context) {
        if (this.firstAired != null) {
            DateFormat dfmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
            return dfmt.format(this.firstAired);
        }
        return context.getResources().getString(R.string.app_text_date_unknown);
    }

    public String getDateEpisodeShortFormatted(Context context) {
        if (this.firstAired != null) {
            DateFormat dfmt = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
            return dfmt.format(this.firstAired);
        }
        return context.getResources().getString(R.string.app_text_date_unknown);
    }

    public String getEpisodeFormatted() {
        String text_ep = EnvironmentConfig.getInstance().getFormatNumber();

        for (int i = 0; i < EnvironmentConfig.typesFormat.length; i++) {
            if (EnvironmentConfig.getInstance().getFormatNumber().equals(EnvironmentConfig.typesFormat[i])) {
                String epN = "";
                if (episodeNumber < 10)
                    epN = "0" + episodeNumber;
                else epN = "" + episodeNumber;
                text_ep = text_ep.replace("1", "" + seasonNumber).replace("02", epN);
                text_ep = text_ep + " " + episodeName;
                break;
            }
        }
        return text_ep;
    }

    public String getNumEpisodeFormatted() {
        String text_ep = EnvironmentConfig.getInstance().getFormatNumber();

        for (int i = 0; i < EnvironmentConfig.typesFormat.length; i++) {
            if (EnvironmentConfig.getInstance().getFormatNumber().equals(EnvironmentConfig.typesFormat[i])) {
                String epN = "";
                if (episodeNumber < 10)
                    epN = "0" + episodeNumber;
                else epN = "" + episodeNumber;
                text_ep = text_ep.replace("1", "" + seasonNumber).replace("02", epN);

                break;
            }
        }
        return text_ep;
    }

    public int getTotalEpisodeNumber() {
        return totalEpisodeNumber;
    }

    public void setTotalEpisodeNumber(int totalEpisodeNumber) {
        this.totalEpisodeNumber = totalEpisodeNumber;
    }

    public Bitmap getBm_poster() {
        return bm_poster;
    }

    public void setBm_poster(Bitmap bm_poster) {
        this.bm_poster = bm_poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEpImgFlag() {
        return epImgFlag;
    }

    public void setEpImgFlag(String epImgFlag) {
        this.epImgFlag = epImgFlag;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(Date firstAired) {
        this.firstAired = firstAired;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getAirAfterSeason() {
        return airAfterSeason;
    }

    public void setAirAfterSeason(String airAfterSeason) {
        this.airAfterSeason = airAfterSeason;
    }

    public String getAirBeforeEpisode() {
        return airBeforeEpisode;
    }

    public void setAirBeforeEpisode(String airBeforeEpisode) {
        this.airBeforeEpisode = airBeforeEpisode;
    }

    public String getAirBeforeSeason() {
        return airBeforeSeason;
    }

    public void setAirBeforeSeason(String airBeforeSeason) {
        this.airBeforeSeason = airBeforeSeason;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getSerieId() {
        return serieId;
    }

    public void setSerieId(Integer serieId) {
        this.serieId = serieId;
    }

    public Date getThumbAdded() {
        return thumbAdded;
    }

    public void setThumbAdded(Date thumbAdded) {
        this.thumbAdded = thumbAdded;
    }

    public String getThumbHight() {
        return thumbHight;
    }

    public void setThumbHight(String thumbHight) {
        this.thumbHight = thumbHight;
    }

    public String getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(String thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public Date getDateWatched() {
        return dateWatched;
    }

    public String getDateWatchedFormatted(Context context) {
        if (this.dateWatched != null) {
            DateFormat dfmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return dfmt.format(this.dateWatched) + " " + sdf.format(this.dateWatched);
        }
        return context.getResources().getString(R.string.app_text_date_unknown);
    }

    public void setDateWatched(Date dateWatched) {
        this.dateWatched = dateWatched;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", \nepisodeNumber=" + episodeNumber +
                ", \nseasonNumber=" + seasonNumber +
                ", \ndirector='" + director + '\'' +
                ", \nepImgFlag='" + epImgFlag + '\'' +
                ", \nepisodeName='" + episodeName + '\'' +
                ", \nfirstAired=" + firstAired +
                ", \nimdb_id='" + imdb_id + '\'' +
                ", \nlanguage='" + language + '\'' +
                ", \noverview='" + overview + '\'' +
                ", \nwriter='" + writer + '\'' +
                ", \nairAfterSeason='" + airAfterSeason + '\'' +
                ", \nairBeforeEpisode='" + airBeforeEpisode + '\'' +
                ", \nairBeforeSeason='" + airBeforeSeason + '\'' +
                ", \nfilename='" + filename + '\'' +
                ", \nbm_poster=" + bm_poster +
                ", \nlastUpdated='" + lastUpdated + '\'' +
                ", \nserieId=" + serieId +
                ", \nthumbAdded=" + thumbAdded +
                ", \nthumbHight='" + thumbHight + '\'' +
                ", \nthumbWidth='" + thumbWidth + '\'' +
                ", \nseasonId='" + seasonId + '\'' +
                '}';
    }
}
