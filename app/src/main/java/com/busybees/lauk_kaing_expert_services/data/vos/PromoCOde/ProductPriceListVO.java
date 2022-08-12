package com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductPriceListVO implements Serializable {

    @SerializedName("price_id")
    @Expose
    private int priceId;

    @SerializedName("item_total")
    @Expose
    private int itemTotal;

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(int itemTotal) {
        this.itemTotal = itemTotal;
    }
}
