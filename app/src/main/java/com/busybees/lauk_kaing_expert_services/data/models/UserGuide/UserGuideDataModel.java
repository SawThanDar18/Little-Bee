package com.busybees.lauk_kaing_expert_services.data.models.UserGuide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGuideDataModel {

    @SerializedName("userguide_en")
    @Expose
    private List<String> en = null;

    @SerializedName("userguide_mm")
    @Expose
    private List<String> mm = null;

    @SerializedName("video")
    @Expose
    private String video;

    @SerializedName("video_id")
    @Expose
    private String videoId;

    public List<String> getEn() {
        return en;
    }

    public void setEn(List<String> en) {
        this.en = en;
    }

    public List<String> getMm() {
        return mm;
    }

    public void setMm(List<String> mm) {
        this.mm = mm;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
