package com.busybees.data.vos.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceAvailableVO {

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

    @SerializedName("onex_image")
    @Expose
    private String serviceImage;

    @SerializedName("products")
    @Expose
    private List<ProductsVO> products;

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

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public List<ProductsVO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsVO> products) {
        this.products = products;
    }
}
