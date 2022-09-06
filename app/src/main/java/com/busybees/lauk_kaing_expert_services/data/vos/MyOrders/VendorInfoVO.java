package com.busybees.lauk_kaing_expert_services.data.vos.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorInfoVO implements Serializable {

    @SerializedName("order_detail_id")
    @Expose
    private Integer orderDetailId;

    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;

    @SerializedName("vcode")
    @Expose
    private String vendorCode;

    @SerializedName("vendor_name")
    @Expose
    private String vendorName;

    @SerializedName("vendor_phone")
    @Expose
    private String vendorPhone;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }
}
