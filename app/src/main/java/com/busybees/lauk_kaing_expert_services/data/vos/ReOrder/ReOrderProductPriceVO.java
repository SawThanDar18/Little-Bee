package com.busybees.lauk_kaing_expert_services.data.vos.ReOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReOrderProductPriceVO {

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("product_price_id")
    @Expose
    private String product_price_id;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_price_id() {
        return product_price_id;
    }

    public void setProduct_price_id(String product_price_id) {
        this.product_price_id = product_price_id;
    }
}
