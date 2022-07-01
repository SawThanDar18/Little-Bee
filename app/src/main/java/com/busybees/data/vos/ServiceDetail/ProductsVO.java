package com.busybees.data.vos.ServiceDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductsVO implements Serializable {

    @SerializedName("service_id")
    @Expose
    private Integer serviceId;

    @SerializedName("product_id")
    @Expose
    private Integer productId;

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

    @SerializedName("product_image")
    @Expose
    private String productImage;

    @SerializedName("sub_products")
    @Expose
    private List<SubProductsVO> subProducts;

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<SubProductsVO> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(List<SubProductsVO> subProducts) {
        this.subProducts = subProducts;
    }
}
