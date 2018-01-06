package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiSeasonObject {

    @SerializedName("ids")
    private ApiIdentifiers identifiers;
    @SerializedName("number")
    private Integer number;

    public ApiIdentifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ApiIdentifiers identifiers) {
        this.identifiers = identifiers;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
