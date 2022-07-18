package com.busybees.data.vos.Address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAddressObject {

    @SerializedName("address_id")
    @Expose
    private int addressId;

    @SerializedName("phone")
    @Expose
    private String phone;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
