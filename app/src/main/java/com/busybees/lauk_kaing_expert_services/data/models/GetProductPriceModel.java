package com.busybees.lauk_kaing_expert_services.data.models;

import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductPriceVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductPriceModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<ProductPriceVO> data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductPriceVO> getData() {
        return data;
    }

    public void setData(List<ProductPriceVO> data) {
        this.data = data;
    }
}
