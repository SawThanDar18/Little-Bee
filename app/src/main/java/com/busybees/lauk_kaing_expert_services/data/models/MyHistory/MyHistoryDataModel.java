package com.busybees.lauk_kaing_expert_services.data.models.MyHistory;

import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyHistoryDataModel implements Serializable {

    @SerializedName("order_completed_date")
    @Expose
    private String orderCompletedDate;

    @SerializedName("reference_no")
    @Expose
    private int referenceNo;

    @SerializedName("order_details")
    @Expose
    private List<MyHistoryDetailVO> myHistoryDetail;

    public String getOrderCompletedDate() {
        return orderCompletedDate;
    }

    public void setOrderCompletedDate(String orderCompletedDate) {
        this.orderCompletedDate = orderCompletedDate;
    }

    public int getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(int referenceNo) {
        this.referenceNo = referenceNo;
    }

    public List<MyHistoryDetailVO> getMyHistoryDetail() {
        return myHistoryDetail;
    }

    public void setMyHistoryDetail(List<MyHistoryDetailVO> myHistoryDetail) {
        this.myHistoryDetail = myHistoryDetail;
    }
}
