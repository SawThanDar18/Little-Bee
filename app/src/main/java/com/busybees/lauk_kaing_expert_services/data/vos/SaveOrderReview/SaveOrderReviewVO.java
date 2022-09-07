package com.busybees.lauk_kaing_expert_services.data.vos.SaveOrderReview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SaveOrderReviewVO implements Serializable {

    @SerializedName("order_detail_id")
    @Expose
    private Integer orderDetailId;

    @SerializedName("vendor_rating_arr")
    @Expose
    private List<VendorRatingVO> vendorRatingArr;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("service_rating")
    @Expose
    private Integer serviceRating;

    @SerializedName("price_rating")
    @Expose
    private Integer priceRating;

    @SerializedName("cust_support_rating")
    @Expose
    private Integer custSupportRating;

    @SerializedName("arrival_on_time_rating")
    @Expose
    private Integer arrivalOnTimeRating;

    @SerializedName("app_rating")
    @Expose
    private Integer appRating;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public List<VendorRatingVO> getVendorRatingArr() {
        return vendorRatingArr;
    }

    public void setVendorRatingArr(List<VendorRatingVO> vendorRatingArr) {
        this.vendorRatingArr = vendorRatingArr;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }

    public Integer getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(Integer priceRating) {
        this.priceRating = priceRating;
    }

    public Integer getCustSupportRating() {
        return custSupportRating;
    }

    public void setCustSupportRating(Integer custSupportRating) {
        this.custSupportRating = custSupportRating;
    }

    public Integer getArrivalOnTimeRating() {
        return arrivalOnTimeRating;
    }

    public void setArrivalOnTimeRating(Integer arrivalOnTimeRating) {
        this.arrivalOnTimeRating = arrivalOnTimeRating;
    }

    public Integer getAppRating() {
        return appRating;
    }

    public void setAppRating(Integer appRating) {
        this.appRating = appRating;
    }


}


