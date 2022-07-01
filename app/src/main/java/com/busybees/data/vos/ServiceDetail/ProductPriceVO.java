package com.busybees.data.vos.ServiceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPriceVO {

    @SerializedName("service_id")
    @Expose
    private Integer serviceId;

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("sub_product_id")
    @Expose
    private Integer subProductId;

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    @SerializedName("service_name_mm")
    @Expose
    private String serviceNameMm;

    @SerializedName("service_name_ch")
    @Expose
    private String serviceNameCh;

    @SerializedName("product_name")
    @Expose
    private String productName;

    @SerializedName("product_name_mm")
    @Expose
    private String productNameMm;

    @SerializedName("product_name_ch")
    @Expose
    private String productNameCh;

    @SerializedName("sub_product_name")
    @Expose
    private String subProductName;

    @SerializedName("sub_product_name_mm")
    @Expose
    private String subProductNameMm;

    @SerializedName("sub_product_name_ch")
    @Expose
    private String subProductNameCh;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("name_mm")
    @Expose
    private String nameMm;

    @SerializedName("name_ch")
    @Expose
    private String nameCh;

    @SerializedName("orderno")
    @Expose
    private Integer orderNo;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("desc")
    @Expose
    private String description;

    @SerializedName("desc_mm")
    @Expose
    private String descriptionMm;

    @SerializedName("desc_ch")
    @Expose
    private String descriptionCh;

    @SerializedName("desc_images")
    @Expose
    private String descriptionImage;

    @SerializedName("original_price")
    @Expose
    private Integer originalPrice;

    @SerializedName("discount_price")
    @Expose
    private Integer discountPrice;

    @SerializedName("discount")
    @Expose
    private Integer discount;

    @SerializedName("bb_amt")
    @Expose
    private Integer bbAmount;

    @SerializedName("form_status")
    @Expose
    private Integer formStatus;

    @SerializedName("is_active")
    @Expose
    private Integer isActive;

    @SerializedName("created_by")
    @Expose
    private String createdBy;

    @SerializedName("updated_by")
    @Expose
    private String updatedBy;

    @SerializedName("created_at")
    @Expose
    private String createdDate;

    @SerializedName("updated_at")
    @Expose
    private String updatedDate;

    @SerializedName("deleted_at")
    @Expose
    private String deletedDate;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameMm() {
        return productNameMm;
    }

    public void setProductNameMm(String productNameMm) {
        this.productNameMm = productNameMm;
    }

    public String getProductNameCh() {
        return productNameCh;
    }

    public void setProductNameCh(String productNameCh) {
        this.productNameCh = productNameCh;
    }

    public String getSubProductName() {
        return subProductName;
    }

    public void setSubProductName(String subProductName) {
        this.subProductName = subProductName;
    }

    public String getSubProductNameMm() {
        return subProductNameMm;
    }

    public void setSubProductNameMm(String subProductNameMm) {
        this.subProductNameMm = subProductNameMm;
    }

    public String getSubProductNameCh() {
        return subProductNameCh;
    }

    public void setSubProductNameCh(String subProductNameCh) {
        this.subProductNameCh = subProductNameCh;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionMm() {
        return descriptionMm;
    }

    public void setDescriptionMm(String descriptionMm) {
        this.descriptionMm = descriptionMm;
    }

    public String getDescriptionCh() {
        return descriptionCh;
    }

    public void setDescriptionCh(String descriptionCh) {
        this.descriptionCh = descriptionCh;
    }

    public String getDescriptionImage() {
        return descriptionImage;
    }

    public void setDescriptionImage(String descriptionImage) {
        this.descriptionImage = descriptionImage;
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

    public Integer getBbAmount() {
        return bbAmount;
    }

    public void setBbAmount(Integer bbAmount) {
        this.bbAmount = bbAmount;
    }

    public Integer getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(Integer formStatus) {
        this.formStatus = formStatus;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
