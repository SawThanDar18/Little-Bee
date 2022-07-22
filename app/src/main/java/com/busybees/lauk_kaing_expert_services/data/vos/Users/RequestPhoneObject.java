package com.busybees.lauk_kaing_expert_services.data.vos.Users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPhoneObject {

    @SerializedName("phone")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
