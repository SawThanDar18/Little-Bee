package com.busybees.little_bee.data.models.ForceUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForeUpdateModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ForceUpdateDataModel result;

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

    public ForceUpdateDataModel getResult() {
        return result;
    }

    public void setResult(ForceUpdateDataModel result) {
        this.result = result;
    }

}
