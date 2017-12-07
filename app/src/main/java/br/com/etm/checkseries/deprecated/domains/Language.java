package br.com.etm.checkseries.deprecated.domains;

/**
 * Created by EDUARDO_MARGOTO on 07/11/2015.
 */
public class Language {

    private Integer id;
    private String abbreviation;
    private String language;

    public Language() {

    }
    public Language(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
