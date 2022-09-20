package com.busybees.little_bee.data.models;
import com.busybees.little_bee.data.vos.Users.UserVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("data")
    @Expose
    private UserVO data;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserVO getData() {
        return data;
    }

    public void setData(UserVO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
