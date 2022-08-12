package com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatchPromoCodeObject implements Serializable {

    @SerializedName("product_price_list")
    @Expose
    private List<ProductPriceListVO> productPriceList;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("promo_code")
    @Expose
    private String promoCode;

    public List<ProductPriceListVO> getProductPriceList() {
        return productPriceList;
    }

    public void setProductPriceList(List<ProductPriceListVO> productPriceList) {
        this.productPriceList = productPriceList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
