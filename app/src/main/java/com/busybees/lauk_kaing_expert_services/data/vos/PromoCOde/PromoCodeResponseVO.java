package com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoCodeResponseVO implements Serializable {

    @SerializedName("discount")
    @Expose
    private int discount;

    @SerializedName("total")
    @Expose
    private int total;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
