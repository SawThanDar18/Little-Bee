package com.busybees.little_bee.data.vos.MyHistory;

import com.busybees.little_bee.data.vos.MyOrders.GeneralFormInfoVO;
import com.busybees.little_bee.data.vos.MyOrders.ProductPriceVO;
import com.busybees.little_bee.data.vos.MyOrders.VendorInfoVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyHistoryDetailVO implements Serializable {

    @SerializedName("rec_number")
    @Expose
    private int recNumber;

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

    @SerializedName("complete_date")
    @Expose
    private String completeDate;

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

    @SerializedName("vendor_info")
    @Expose
    private List<VendorInfoVO> vendorData;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("order_address")
    @Expose
    private String orderAddress;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("general_forminfo")
    @Expose
    private GeneralFormInfoVO generalFormInfo;

    @SerializedName("over_all_rating")
    @Expose
    private int overAllRating;

    public int getRecNumber() {
        return recNumber;
    }

    public void setRecNumber(int recNumber) {
        this.recNumber = recNumber;
    }

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

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
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

    public List<VendorInfoVO> getVendorData() {
        return vendorData;
    }

    public void setVendorData(List<VendorInfoVO> vendorData) {
        this.vendorData = vendorData;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public int getOverAllRating() {
        return overAllRating;
    }

    public void setOverAllRating(int overAllRating) {
        this.overAllRating = overAllRating;
    }
}