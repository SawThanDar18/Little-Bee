package com.busybees.little_bee.data.models.GetCart;

import com.busybees.little_bee.data.vos.PromoCOde.PromoCodeVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCartModel {

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("item_total")
    @Expose
    private int itemTotal;

    @SerializedName("discount_total_all")
    @Expose
    private int discountTotalAll;

    @SerializedName("payable_amount")
    @Expose
    private int payableAmount;

    @SerializedName("total_quantity")
    @Expose
    private int totalQuantity;

    @SerializedName("data")
    @Expose
    private List<GetCartDataModel> data;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("promolist")
    @Expose
    private List<PromoCodeVO> promoCodeList;

    @SerializedName("continue_alert_msg")
    @Expose
    private String continueAlertMsg;

    @SerializedName("continue_error")
    @Expose
    private Boolean continueError;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(int itemTotal) {
        this.itemTotal = itemTotal;
    }

    public int getDiscountTotalAll() {
        return discountTotalAll;
    }

    public void setDiscountTotalAll(int discountTotalAll) {
        this.discountTotalAll = discountTotalAll;
    }

    public int getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(int payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<GetCartDataModel> getData() {
        return data;
    }

    public void setData(List<GetCartDataModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<PromoCodeVO> getPromoCodeList() {
        return promoCodeList;
    }

    public void setPromoCodeList(List<PromoCodeVO> promoCodeList) {
        this.promoCodeList = promoCodeList;
    }

    public String getContinueAlertMsg() {
        return continueAlertMsg;
    }

    public void setContinueAlertMsg(String continueAlertMsg) {
        this.continueAlertMsg = continueAlertMsg;
    }

    public Boolean getContinueError() {
        return continueError;
    }

    public void setContinueError(Boolean continueError) {
        this.continueError = continueError;
    }
}


