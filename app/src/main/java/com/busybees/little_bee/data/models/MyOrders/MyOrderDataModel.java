package com.busybees.little_bee.data.models.MyOrders;

import com.busybees.little_bee.data.vos.MyOrders.MyOrdersDetailVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyOrderDataModel implements Serializable {

    @SerializedName("order_created_date")
    @Expose
    private String orderCreatedDate;

    @SerializedName("reference_no")
    @Expose
    private String referenceNo;

    @SerializedName("order_details")
    @Expose
    private List<MyOrdersDetailVO> ordersDetail;

    public String getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(String orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public List<MyOrdersDetailVO> getOrdersDetail() {
        return ordersDetail;
    }

    public void setOrdersDetail(List<MyOrdersDetailVO> ordersDetail) {
        this.ordersDetail = ordersDetail;
    }
}
