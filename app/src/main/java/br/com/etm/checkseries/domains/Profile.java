package br.com.etm.checkseries.domains;

import java.io.Serializable;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class Profile implements Serializable {

    static Profile profile = new Profile();

    private String id = "";
    private String name = "";
    private String username = "";
    private String email = "";
    private String language = "";
    private String imageUrl = "";
    private String tokenFB = "";

    private Profile() {
    }

    public static Profile getInstance() {
        if (profile == null) return new Profile();
        else return profile;
    }

    public String getTokenFB() {
        return profile.tokenFB;
    }

    public void setTokenFB(String tokenFB) {
        profile.tokenFB = tokenFB;
    }

    public String getId() {
        return profile.id;
    }

    public void setId(String id) {
        profile.id = id;
    }

    public String getName() {
        return profile.name;
    }

    public void setName(String name) {
        profile.name = name;
    }

    public String getUsername() {
        return profile.username;
    }

    public void setUsername(String username) {
        profile.username = username;
    }

    public String getEmail() {
        return profile.email;
    }

    public void setEmail(String email) {
        profile.email = email;
    }

    public String getLanguage() {
        return profile.language;
    }

    public void setLanguage(String language) {
        profile.language = language;
    }

    public String getImageUrl() {
        return profile.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        profile.imageUrl = imageUrl;
    }

    public void clear() {
        profile = new Profile();
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", imageUrl='" + getImageUrl() + '\'' +
                '}';
    }
}
