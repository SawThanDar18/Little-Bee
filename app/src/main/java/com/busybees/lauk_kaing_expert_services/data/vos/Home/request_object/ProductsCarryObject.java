package com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductsCarryObject implements Serializable {

    @SerializedName("service_id")
    @Expose
    private int serviceId;

    @SerializedName("product_id")
    @Expose
    private int productId;

    @SerializedName("sub_product_id")
    @Expose
    private int subProductId;

    @SerializedName("product_price_id")
    @Expose
    private int productPriceId;

    @SerializedName("step")
    @Expose
    private int step;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSubProductId() {
        return subProductId;
    }

    public void setSubProductId(int subProductId) {
        this.subProductId = subProductId;
    }

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
