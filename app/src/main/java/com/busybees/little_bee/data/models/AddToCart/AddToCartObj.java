package com.busybees.little_bee.data.models.AddToCart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToCartObj {

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("product_price_id")
    @Expose
    private int productPriceId;

    @SerializedName("form_status")
    @Expose
    private int formStatus;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public int getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(int formStatus) {
        this.formStatus = formStatus;
    }
}
