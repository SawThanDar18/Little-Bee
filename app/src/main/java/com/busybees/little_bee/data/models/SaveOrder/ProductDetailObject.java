package com.busybees.little_bee.data.models.SaveOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetailObject implements Serializable {

    @SerializedName("price_id")
    @Expose
    private int priceId;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
