package com.busybees.lauk_kaing_expert_services.data.models;

import com.busybees.lauk_kaing_expert_services.data.vos.Address.AddressVO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAddressModel {

    @SerializedName("error")
    @Expose
    private Boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<AddressVO> data;

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

    public List<AddressVO> getResult() {
        return data;
    }

    public void setResult(List<AddressVO> result) {
        this.data = result;
    }
}
