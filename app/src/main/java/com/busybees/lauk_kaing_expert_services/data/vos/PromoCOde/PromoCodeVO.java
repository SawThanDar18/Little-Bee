package com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoCodeVO implements Serializable {

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    @SerializedName("service_name_mm")
    @Expose
    private String serviceNameMm;

    @SerializedName("service_name_ch")
    @Expose
    private String serviceNameCh;

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

    @SerializedName("prmo_id")
    @Expose
    private int promoId;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameMm() {
        return serviceNameMm;
    }

    public void setServiceNameMm(String serviceNameMm) {
        this.serviceNameMm = serviceNameMm;
    }

    public String getServiceNameCh() {
        return serviceNameCh;
    }

    public void setServiceNameCh(String serviceNameCh) {
        this.serviceNameCh = serviceNameCh;
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

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }
}
