package com.busybees.little_bee.data.models.UserGuide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserGuideModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private UserGuideDataModel data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserGuideDataModel getData() {
        return data;
    }

    public void setData(UserGuideDataModel data) {
        this.data = data;
    }


}
