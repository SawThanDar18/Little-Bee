package com.busybees.lauk_kaing_expert_services.data.models.GetCart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCartObj {

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
