package com.busybees.lauk_kaing_expert_services.data.vos.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdsVO implements Serializable {

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("img")
    @Expose
    private String image;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
