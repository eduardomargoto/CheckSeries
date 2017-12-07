package br.com.etm.checkseries.deprecated.domains;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by EDUARDO_MARGOTO on 13/04/2016.
 */
public class Comment {

    private String id;
    private String comment;
    private String spoiler;
    private String date_comment;

    private String id_user;
    private String name_user;
    private String image_url;
    private String serieID;
    private String episodeID;

    public Comment() {
        date_comment = dateFormatted();
        id = generatedId();
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEpisodeID() {
        return episodeID;
    }

    public void setEpisodeID(String episodeID) {
        this.episodeID = episodeID;
    }

    public String getSerieID() {
        return serieID;
    }

    public void setSerieID(String serieID) {
        this.serieID = serieID;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String generatedId() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public String getId() {
        return id;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSpoiler() {
        return spoiler;
    }

    public void setSpoiler(String spoiler) {
        this.spoiler = spoiler;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

    public String dateFormatted() {
        Calendar date = Calendar.getInstance();
        if (this.date_comment != null)
            date.setTimeInMillis(Long.valueOf(this.date_comment));

        DateFormat dfmt = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        String dia_da_semana = dfmt.format(date.getTime()).substring(0, 3);
        dfmt = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
        return "(" + dia_da_semana + ") " + dfmt.format(date.getTime());
    }

    public String getDate_comment() {

        return date_comment;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", spoiler=" + spoiler +
                ", date_comment=" + date_comment +
                ", name_user='" + name_user + '\'' +
                ", image_url='" + image_url + '\'' +
                ", serieID='" + serieID + '\'' +
                ", episodeID='" + episodeID + '\'' +
                '}';
    }
}
