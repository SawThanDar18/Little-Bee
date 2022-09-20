package com.busybees.little_bee.data.vos.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductPriceVO implements Serializable {

    @SerializedName("product_price_id")
    @Expose
    private int productPriceId;

    @SerializedName("name")
    @Expose
    private String serviceName;

    @SerializedName("name_mm")
    @Expose
    private String serviceNameMm;

    @SerializedName("name_ch")
    @Expose
    private String serviceNameCh;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("total_discount")
    @Expose
    private int totalDiscount;

    @SerializedName("promo_totaldiscount")
    @Expose
    private int promoTotalDiscount;

    @SerializedName("total_price")
    @Expose
    private int totalPrice;

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public int getPromoTotalDiscount() {
        return promoTotalDiscount;
    }

    public void setPromoTotalDiscount(int promoTotalDiscount) {
        this.promoTotalDiscount = promoTotalDiscount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
