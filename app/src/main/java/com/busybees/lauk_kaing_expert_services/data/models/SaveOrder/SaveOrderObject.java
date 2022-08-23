package com.busybees.lauk_kaing_expert_services.data.models.SaveOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SaveOrderObject implements Serializable {

    @SerializedName("promo_id")
    @Expose
    private int promoId;

    @SerializedName("product_detail")
    @Expose
    private List<ProductDetailObject> productDetail;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("order_source")
    @Expose
    private String orderSource;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("payment_type")
    @Expose
    private String paymentType;

    @SerializedName("address")
    @Expose
    private int addressId;

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public List<ProductDetailObject> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<ProductDetailObject> productDetail) {
        this.productDetail = productDetail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
