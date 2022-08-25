package com.busybees.lauk_kaing_expert_services.data.vos.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyOrdersDetailVO implements Serializable {

    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("original_total")
    @Expose
    private int originalTotal;

    @SerializedName("total_discount")
    @Expose
    private int totalDiscount;

    @SerializedName("promo_total_discount")
    @Expose
    private int promoTotalDiscount;

    @SerializedName("total_price")
    @Expose
    private int totalPrice;

    @SerializedName("product_price")
    @Expose
    private List<ProductPriceVO> productPrice;

    @SerializedName("order_address")
    @Expose
    private String orderAddress;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("general_forminfo")
    @Expose
    private GeneralFormInfoVO generalFormInfo;

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getOriginalTotal() {
        return originalTotal;
    }

    public void setOriginalTotal(int originalTotal) {
        this.originalTotal = originalTotal;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getPromoTotalDiscount() {
        return promoTotalDiscount;
    }

    public void setPromoTotalDiscount(int promoTotalDiscount) {
        this.promoTotalDiscount = promoTotalDiscount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductPriceVO> getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(List<ProductPriceVO> productPrice) {
        this.productPrice = productPrice;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GeneralFormInfoVO getGeneralFormInfo() {
        return generalFormInfo;
    }

    public void setGeneralFormInfo(GeneralFormInfoVO generalFormInfo) {
        this.generalFormInfo = generalFormInfo;
    }
}
