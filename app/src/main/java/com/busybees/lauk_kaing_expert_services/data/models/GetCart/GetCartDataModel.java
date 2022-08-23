package com.busybees.lauk_kaing_expert_services.data.models.GetCart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCartDataModel {

    @SerializedName("id")
    @Expose
    private Integer id;

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

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("original_price")
    @Expose
    private Integer originalPrice;

    @SerializedName("discount_price")
    @Expose
    private Integer discountPrice;

    @SerializedName("discount")
    @Expose
    private Integer discount;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("name_mm")
    @Expose
    private String nameMm;

    @SerializedName("name_ch")
    @Expose
    private String nameCh;

    @SerializedName("form_status")
    @Expose
    private Integer formStatus;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("discount_total")
    @Expose
    private Integer discountTotal;

    @SerializedName("discount_total_price")
    @Expose
    private Integer discountTotalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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

    public Integer getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(Integer formStatus) {
        this.formStatus = formStatus;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(Integer discountTotal) {
        this.discountTotal = discountTotal;
    }

    public Integer getDiscountTotalPrice() {
        return discountTotalPrice;
    }

    public void setDiscountTotalPrice(Integer discountTotalPrice) {
        this.discountTotalPrice = discountTotalPrice;
    }
}
