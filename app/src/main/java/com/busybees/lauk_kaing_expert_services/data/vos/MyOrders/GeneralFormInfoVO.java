package com.busybees.lauk_kaing_expert_services.data.vos.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GeneralFormInfoVO implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("product_price_id")
    @Expose
    private int productPriceId;

    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("budget")
    @Expose
    private int budget;

    @SerializedName("confim_price")
    @Expose
    private int confirmPrice;

    @SerializedName("square_feet")
    @Expose
    private String squareFeet;

    @SerializedName("images")
    @Expose
    private List<String> images;

    @SerializedName("detail_text")
    @Expose
    private String detailText;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("imageslink")
    @Expose
    private String imagesLink;

    @SerializedName("form_status")
    @Expose
    private int formStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductPriceId() {
        return productPriceId;
    }

    public void setProductPriceId(int productPriceId) {
        this.productPriceId = productPriceId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getConfirmPrice() {
        return confirmPrice;
    }

    public void setConfirmPrice(int confirmPrice) {
        this.confirmPrice = confirmPrice;
    }

    public String getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(String squareFeet) {
        this.squareFeet = squareFeet;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImagesLink() {
        return imagesLink;
    }

    public void setImagesLink(String imagesLink) {
        this.imagesLink = imagesLink;
    }

    public int getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(int formStatus) {
        this.formStatus = formStatus;
    }
}
