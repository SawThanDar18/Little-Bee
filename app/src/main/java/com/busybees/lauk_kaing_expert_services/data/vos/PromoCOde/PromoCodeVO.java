package com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoCodeVO implements Serializable {

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    @SerializedName("prmo_code")
    @Expose
    private String promoCode;

    @SerializedName("prmo_name")
    @Expose
    private String promoName;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("promo_active")
    @Expose
    private int promoActive;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getPromoActive() {
        return promoActive;
    }

    public void setPromoActive(int promoActive) {
        this.promoActive = promoActive;
    }
}
