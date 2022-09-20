package com.busybees.little_bee.data.models;

import com.busybees.little_bee.data.vos.PromoCOde.PromoCodeResponseVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MatchPromoCodeModel implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private PromoCodeResponseVO data;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PromoCodeResponseVO getData() {
        return data;
    }

    public void setData(PromoCodeResponseVO data) {
        this.data = data;
    }
}
