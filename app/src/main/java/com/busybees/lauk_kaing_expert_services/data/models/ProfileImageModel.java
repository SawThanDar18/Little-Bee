package com.busybees.lauk_kaing_expert_services.data.models;

import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private UserVO data;

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

    public UserVO getData() {
        return data;
    }

    public void setData(UserVO data) {
        this.data = data;
    }
}
