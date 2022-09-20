package com.busybees.little_bee.data.vos.Address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditAddressObject {
    @SerializedName("address_id")
    @Expose
    private String addressId;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("lat")
    @Expose
    private String latitude;

    @SerializedName("lng")
    @Expose
    private String longitude;

    @SerializedName("loc_name")
    @Expose
    private String locationName;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
