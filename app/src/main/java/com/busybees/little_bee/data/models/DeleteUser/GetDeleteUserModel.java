package com.busybees.little_bee.data.models.DeleteUser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDeleteUserModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

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
}
