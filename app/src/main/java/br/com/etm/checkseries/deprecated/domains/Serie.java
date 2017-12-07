package br.com.etm.checkseries.deprecated.domains;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import br.com.etm.checkseries.R;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class Serie implements Serializable, Cloneable {

    private Integer id;
    private Integer serieid;
    private String name;
    private String alias_names; // outros nomes, em jap, ingles etc separados por | ;
    private String overview;
    private Date first_aired;
    private String runtime; // tempo de duracao de episodios
    private String network;
    private String banner;
    private String poster; //poster/<id>-<number>.jpg
    private String fanArt;
    private String imdb_id;
    private String zap2it_id;
    private String language;
    private String last_updated; // ultima atualizacao (Unix time)
    private String status; // status (ex.: Ended, em exibicao)
    private String actors; // lista separada por | (ex:. eu|ele
    private String airs_DayOfWeek; // dia da semana, que é exibido
    private String airs_Time; // hora que é exibida ex.: 8:00 PM
    private String genre; // generos separados por | (ex.: action|adventure)
    private String rating;
    private int ratingCount;
    private boolean favorite = false;
    private boolean hidden = false;

    private Bitmap bm_poster;
    private boolean added = false;
    private Episode nextEpisode;
    private int totalEpisodes;
    private List<Episode> episodeList;

    public Serie() {
    }

    public Serie clone() {

        Serie serie = new Serie();
        serie.setId(this.id);
        serie.setSerieid(this.serieid);
        serie.setName(this.name);
        serie.setAlias_names(this.alias_names);
        serie.setOverview(this.overview);
        serie.setFirst_aired(this.first_aired);
        serie.setRuntime(this.runtime);
        serie.setNetwork(this.network);
        serie.setBanner(this.banner);
        serie.setPoster(this.poster);
        serie.setFanArt(this.fanArt);
        serie.setImdb_id(this.imdb_id);
        serie.setZap2it_id(this.zap2it_id);
        serie.setLanguage(this.language);
        serie.setLast_updated(this.last_updated);
        serie.setStatus(this.status);
        serie.setActors(this.actors);
        serie.setAirs_DayOfWeek(this.airs_DayOfWeek);
        serie.setAirs_Time(this.airs_Time);
        serie.setGenre(this.genre);
        serie.setRating(this.rating);
        serie.setRatingCount(this.ratingCount);
        serie.setFavorite(this.favorite);
        serie.setHidden(this.hidden);
        serie.setBm_poster(this.bm_poster);
        serie.setAdded(this.added);

        return serie;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<Episode> getEpisodeList() {
        if (episodeList == null) episodeList = new ArrayList<>();
        return episodeList;
    }

    public int getSizeEpisodes() {

        if (episodeList == null) episodeList = new ArrayList<>();
        int size = 0;
        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials()) {
            for (Episode ep : episodeList) {
                if (ep.getSeasonNumber() != 0)
                    size++;
            }
        } else {
            for (Episode ep : episodeList) {
                size++;
            }
        }
        return size;
    }

    public int getSizeEpisodesNotDisplayed() {
        if (episodeList == null) episodeList = new ArrayList<>();
        int size = 0;
        Calendar c = Calendar.getInstance();

        for (Episode ep : episodeList) {
            if (c.getTime().before(ep.getFirstAired()))
                size++;
        }

        return size;
    }

    public int getSizeEpisodesNotDisplayed(int season_id) {
        if (episodeList == null) episodeList = new ArrayList<>();
        int size = 0;
        Calendar c = Calendar.getInstance();
        try {
            for (Episode ep : episodeList) {
                if (ep.getFirstAired() != null) {
                    if (c.getTime().before(ep.getFirstAired()) && ep.getSeasonNumber() == season_id)
                        size++;
                }
            }

            return size;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public int getSizeSeasons() {
        if (episodeList == null) episodeList = new ArrayList<>();
        if (!episodeList.isEmpty()) {
            int size = 1;
            int last = episodeList.get(0).getSeasonNumber();

            for (Episode ep : episodeList) {
                if (ep.getSeasonNumber() != last) {
                    last = ep.getSeasonNumber();
                    size++;
                }
            }

            return size;
        } else return 0;
    }

    public int getSizeEpisodes(int season_id) {
        if (episodeList == null) episodeList = new ArrayList<>();
        if (!episodeList.isEmpty()) {
            int size = 0;
            for (Episode ep : episodeList) {
                if (ep.getSeasonNumber() == season_id) {
                    size++;
                }
            }

            return size;
        } else return 0;
    }

    public int getSizeEpisodesWatched(int season_id) {
        if (episodeList == null) episodeList = new ArrayList<>();
        if (!episodeList.isEmpty()) {
            int size = 0;
            for (Episode ep : episodeList) {
                if (ep.getSeasonNumber() == season_id && (ep.isWatched() || ep.isSkipped())) {
                    size++;
                }
            }

            return size;
        } else return 0;
    }

    public int getSizeWatchedEpisode() {
        if (episodeList == null) episodeList = new ArrayList<>();
        int size = 0;
        for (Episode ep : episodeList) {
            if (!(EnvironmentConfig.getInstance().isHiddenEpisodesSpecials() && ep.getSeasonNumber() == 0) && (ep.isWatched() || ep.isSkipped()))
                size++;

        }
        return size;
    }

    public Episode getNextEpisode() {
        if(nextEpisode != null)
            return nextEpisode;


        if (episodeList == null) episodeList = new ArrayList<>();

        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials()) {
            for (int i = 0; i < episodeList.size(); i++) {
                if (episodeList.get(i).getSeasonNumber() != 0 && (!episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped())) {
                    return episodeList.get(i);
                }
            }
        } else {
            for (int i = 0; i < episodeList.size(); i++) {
                if (!episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped()) {
                    return episodeList.get(i);
                }
            }
        }
        return null;
    }

    public int getPosition(Episode episode) {
        if (episodeList == null) episodeList = new ArrayList<>();
        for (int i = 0; i < episodeList.size(); i++) {
            if (episodeList.get(i).getId() == episode.getId()) {
                return i;
            }
        }
        return -1;
    }

    public int getPositionNextEpisode() {
        if (episodeList == null) episodeList = new ArrayList<>();

        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials()) {
            for (int i = 0; i < episodeList.size(); i++) {
                if (episodeList.get(i).getSeasonNumber() != 0 && (!episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped())) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < episodeList.size(); i++) {
                if (!episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSerieid(Integer serieid) {
        this.serieid = serieid;
    }

    public Integer getSerieid() {
        return serieid;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getFanArt() {
        return fanArt;
    }

    public String getFanArtFilenameCache() {
        return fanArt.replaceAll("/", "-").replaceAll(".jpg", "");
    }
    public String getPosterFilenameCache() {
        return poster.replaceAll("/", "-").replaceAll(".jpg", "");
    }


    public String getFanArtFilename() {
        return fanArt.replaceAll("/", "-");
    }

    public void setFanArt(String fanArt) {
        this.fanArt = fanArt;
    }

    public String getZap2it_id() {
        return zap2it_id;
    }

    public void setZap2it_id(String zap2it_id) {
        this.zap2it_id = zap2it_id;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusFormatted() {
        if (status.equals("Ended"))
            return R.string.app_serie_status_ended;
        else return R.string.app_serie_status_continuing; // Continuing

    }

    public boolean haveSpecialSeason() {
        if (episodeList == null) episodeList = new ArrayList<>();
        for (int i = 0; i < episodeList.size(); i++) {
            if (episodeList.get(i).getSeasonNumber() == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isAllEpisodeWatched() {
        if(nextEpisode != null)
            return false;
        if (episodeList == null) episodeList = new ArrayList<>();

        if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials()) {
            for (int i = 0; i < episodeList.size(); i++) {
                if (episodeList.get(i).getSeasonNumber() != 0 && !episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped()) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < episodeList.size(); i++) {
                if (!episodeList.get(i).isWatched() && !episodeList.get(i).isSkipped()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEnded() {
        if (status.equals("Ended"))
            return true;
        else return false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getAirs_DayOfWeek() {
        return airs_DayOfWeek;
    }

    public String getAirs_DayOfWeekFormatted() {
        String[] dayofWeek_en = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        DateFormat dfmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        int position = -1;
        Calendar c = Calendar.getInstance();

        if (!airs_DayOfWeek.equals("")) {
            for (int i = 0; i < dayofWeek_en.length; i++) {
                if (airs_DayOfWeek.equals(dayofWeek_en[i])) {
                    if (!airs_Time.isEmpty()) {
                        String hora = String.valueOf(Integer.parseInt(airs_Time.split(":")[0]) + EnvironmentConfig.getInstance().getTimeOffset());
                        if (Integer.parseInt(hora) > 24)
                            c.set(Calendar.DAY_OF_WEEK, i + 1);
                        else c.set(Calendar.DAY_OF_WEEK, i);
                    } else c.set(Calendar.DAY_OF_WEEK, i);
                }
            }

            return dfmt.format(c.getTime()).substring(0, 3);
        } else return "";
    }

    public void setAirs_DayOfWeek(String airs_DayOfWeek) {
        this.airs_DayOfWeek = airs_DayOfWeek;
    }

    public String getAirs_Time() {

        if (airs_Time.toUpperCase().indexOf("PM") != -1) { //08:00 PM
            String aux = airs_Time.substring(0, 2);
            aux = aux.replace(":", "");
            aux = String.valueOf(Integer.parseInt(aux) + 12);
            return aux + ":" + airs_Time.substring(2, 5);
        } else if (airs_Time.toUpperCase().indexOf("AM") != -1) {
            return airs_Time.substring(0, airs_Time.length() - 2);

        }

        String hora, min = "";
        if (airs_Time.contains(":")) {
//            hora = airs_Time.split(":")[0];
            if (airs_Time.split(":").length > 1)
                min = airs_Time.split(":")[1];
            hora = String.valueOf(Integer.parseInt(airs_Time.split(":")[0]) + EnvironmentConfig.getInstance().getTimeOffset());
            if (Integer.parseInt(hora) > 24)
                hora = String.valueOf(Integer.parseInt(hora) - 24);
            return hora + ":" + min;
        }
        return "";
    }

    public void setAirs_Time(String airs_Time) {
        this.airs_Time = airs_Time;
    }

    public String getGenre() {
        return genre;
    }

    public String getGenreFormatted() {

        return genre.replaceAll(Pattern.quote("|"), ",").substring(1, genre.length() - 1);
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Serie(String name) {
        this.name = name;
    }

    public Bitmap getBm_poster() {
        return bm_poster;
    }

    public void setBm_poster(Bitmap bm_poster) {
        this.bm_poster = bm_poster;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImdb_id() {
        if (imdb_id == null)
            return "";
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getName() {
        return name;
    }

    public String getPoster() {
        return poster;
    }

    public String getAlias_names() {
        if (alias_names == null)
            return "";
        return alias_names;
    }

    public void setAlias_names(String alias_names) {
        this.alias_names = alias_names;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Date first_aired) {
        this.first_aired = first_aired;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getNetwork() {
        return network != null ? network : "";
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setNextEpisode(Episode nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "id=" + id +
                ", serieid=" + serieid +
                ", name='" + name + '\'' +
                ", alias_names='" + alias_names + '\'' +
                ", overview='" + overview + '\'' +
                ", first_aired=" + first_aired +
                ", runtime='" + runtime + '\'' +
                ", network='" + network + '\'' +
                ", banner='" + banner + '\'' +
                ", poster='" + poster + '\'' +
                ", fanArt='" + fanArt + '\'' +
                ", imdb_id='" + imdb_id + '\'' +
                ", zap2it_id='" + zap2it_id + '\'' +
                ", language='" + language + '\'' +
                ", last_updated='" + last_updated + '\'' +
                ", status='" + status + '\'' +
                ", actors='" + actors + '\'' +
                ", airs_DayOfWeek='" + airs_DayOfWeek + '\'' +
                ", airs_Time='" + airs_Time + '\'' +
                ", genre='" + genre + '\'' +
                ", bm_poster=" + bm_poster +
                ", added=" + added +
                ", favorite=" + favorite +
                ", hidden=" + hidden +

                /*", episodeList=" + episodeList +*/
                '}';
    }


}
