package com.busybees.little_bee.data.models.GetCart;

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
