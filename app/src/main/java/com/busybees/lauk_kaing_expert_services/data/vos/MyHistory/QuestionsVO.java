package com.busybees.lauk_kaing_expert_services.data.vos.MyHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuestionsVO implements Serializable {

    @SerializedName("service_rating")
    @Expose
    private String serviceRating;

    @SerializedName("vendor_rating")
    @Expose
    private String vendorRating;

    @SerializedName("arrival_on_time_rating")
    @Expose
    private String arrivalOnTimeRating;

    @SerializedName("price_rating")
    @Expose
    private String priceRating;

    @SerializedName("cust_support_rating")
    @Expose
    private String customerSupportRating;

    @SerializedName("app_rating")
    @Expose
    private String appRating;

    @SerializedName("comment")
    @Expose
    private String comment;

    public String getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(String serviceRating) {
        this.serviceRating = serviceRating;
    }

    public String getVendorRating() {
        return vendorRating;
    }

    public void setVendorRating(String vendorRating) {
        this.vendorRating = vendorRating;
    }

    public String getArrivalOnTimeRating() {
        return arrivalOnTimeRating;
    }

    public void setArrivalOnTimeRating(String arrivalOnTimeRating) {
        this.arrivalOnTimeRating = arrivalOnTimeRating;
    }

    public String getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(String priceRating) {
        this.priceRating = priceRating;
    }

    public String getCustomerSupportRating() {
        return customerSupportRating;
    }

    public void setCustomerSupportRating(String customerSupportRating) {
        this.customerSupportRating = customerSupportRating;
    }

    public String getAppRating() {
        return appRating;
    }

    public void setAppRating(String appRating) {
        this.appRating = appRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
