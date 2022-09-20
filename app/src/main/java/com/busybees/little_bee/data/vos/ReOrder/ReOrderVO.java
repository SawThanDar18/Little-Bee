package com.busybees.little_bee.data.vos.ReOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReOrderVO implements Serializable {

    @SerializedName("product_price_arr")
    @Expose
    private ReOrderProductPriceVO productPriceObject;

    @SerializedName("phone")
    @Expose
    private String phone;

    public ReOrderProductPriceVO getProductPriceObject() {
        return productPriceObject;
    }

    public void setProductPriceObject(ReOrderProductPriceVO productPriceObject) {
        this.productPriceObject = productPriceObject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
