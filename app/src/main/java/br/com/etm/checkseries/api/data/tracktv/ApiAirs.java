package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiAirs {

    @SerializedName("day")
    private String day;
    @SerializedName("time")
    private String hour;
    @SerializedName("timezone")
    private String timezone;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
