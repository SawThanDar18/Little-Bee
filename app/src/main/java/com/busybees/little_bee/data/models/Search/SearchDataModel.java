package com.busybees.little_bee.data.models.Search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchDataModel {

    @SerializedName("sub_product_id")
    @Expose
    private Integer subProductId;

    @SerializedName("Product_id")
    @Expose
    private Integer productId;

    @SerializedName("parent_id")
    @Expose
    private Integer parentId;

    @SerializedName("service_id")
    @Expose
    private Integer serviceId;

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

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("sub_image")
    @Expose
    private String subImage;

    public Integer getSubProductId() {
        return subProductId;
    }

    public void setSubProductId(Integer subProductId) {
        this.subProductId = subProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }
}
