package com.busybees.data.models;

import com.busybees.data.vos.Home.GetAllHomeVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllHomeModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private GetAllHomeVO data;

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

    public GetAllHomeVO getData() {
        return data;
    }

    public void setData(GetAllHomeVO data) {
        this.data = data;
    }
}
