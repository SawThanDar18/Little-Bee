package com.busybees.lauk_kaing_expert_services.data.models.SaveToken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveTokenObj {

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("token")
    @Expose
    private String token;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

