package com.busybees.little_bee.data.vos.Users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendOTPObject {

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
