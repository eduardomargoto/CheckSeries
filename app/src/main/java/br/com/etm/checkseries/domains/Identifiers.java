package br.com.etm.checkseries.domains;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eduardo on 01/12/17.
 */

public class Identifiers implements Serializable {

    @SerializedName("trakt")
    private Integer trakt;
    @SerializedName("tvdb")
    private Integer tvdb;
    @SerializedName("slug")
    private String slug;
    @SerializedName("imdb")
    private String imdb;
    @SerializedName("tmdb")
    private Integer tmdb;
    @SerializedName("tvrage")
    private Integer tvrage;

    public Integer getTvdb() {
        return tvdb;
    }

    public void setTvdb(Integer tvdb) {
        this.tvdb = tvdb;
    }

    public Integer getTrakt() {
        return trakt;
    }

    public void setTrakt(Integer trakt) {
        this.trakt = trakt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Integer getTmdb() {
        return tmdb;
    }

    public void setTmdb(Integer tmdb) {
        this.tmdb = tmdb;
    }

    public Integer getTvrage() {
        return tvrage;
    }

    public void setTvrage(Integer tvrage) {
        this.tvrage = tvrage;
    }
}
