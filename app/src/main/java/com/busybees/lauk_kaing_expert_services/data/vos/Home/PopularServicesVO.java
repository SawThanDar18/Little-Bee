package com.busybees.lauk_kaing_expert_services.data.vos.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularServicesVO {

    @SerializedName("service_id")
    @Expose
    private Integer serviceId;

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("sub_product_id")
    @Expose
    private Integer subProductId;

    @SerializedName("product_price_id")
    @Expose
    private Integer productPriceId;

    @SerializedName("step")
    @Expose
    private Integer step;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("name_mm")
    @Expose
    private String nameMm;

    @SerializedName("name_ch")
    @Expose
    private String nameCh;

    @SerializedName("popular_image")
    @Expose
    private String popularImage;

    @SerializedName("original_price")
    @Expose
    private Integer originalPrice;

    @SerializedName("discount_price")
    @Expose
    private Integer discountPrice;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSubProductId() {
        return subProductId;
    }

    public void setSubProductId(Integer subProductId) {
        this.subProductId = subProductId;
    }

    public Integer getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(Integer productPriceId) {
        this.productPriceId = productPriceId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameMm() {
        return nameMm;
    }

    public void setNameMm(String nameMm) {
        this.nameMm = nameMm;
    }

    public String getNameCh() {
        return nameCh;
    }

    public void setNameCh(String nameCh) {
        this.nameCh = nameCh;
    }

    public String getPopularImage() {
        return popularImage;
    }

    public void setPopularImage(String popularImage) {
        this.popularImage = popularImage;
    }

    public Integer getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }
}
