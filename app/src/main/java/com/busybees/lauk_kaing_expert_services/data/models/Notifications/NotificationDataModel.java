package com.busybees.lauk_kaing_expert_services.data.models.Notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationDataModel implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("status")
    @Expose
    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
