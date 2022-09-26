package com.busybees.little_bee.data.models.ForceUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForceUpdateDataModel {

    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
