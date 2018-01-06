package br.com.etm.checkseries.api.data.tracktv;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eduardo on 07/12/17.
 */

public class ApiPersonObject {

    @SerializedName("name")
    private String name;
    @SerializedName("ids")
    private ApiIdentifiers identifiers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApiIdentifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ApiIdentifiers identifiers) {
        this.identifiers = identifiers;
    }
}
