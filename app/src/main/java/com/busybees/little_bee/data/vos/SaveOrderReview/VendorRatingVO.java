package com.busybees.little_bee.data.vos.SaveOrderReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorRatingVO implements Serializable {

    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;

    @SerializedName("vendor_rating")
    @Expose
    private Integer vendorRating;

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getVendorRating() {
        return vendorRating;
    }

    public void setVendorRating(Integer vendorRating) {
        this.vendorRating = vendorRating;
    }
}
