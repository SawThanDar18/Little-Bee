package com.busybees.little_bee.data.models.GetCart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificArray {
    @SerializedName("price_id")
    @Expose
    private Integer price_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_mm")
    @Expose
    private String name_mm;
    @SerializedName("name_ch")
    @Expose
    private String name_ch;
    @SerializedName("service_id")
    @Expose
    private int serviceId;

    public Integer getPrice_id() {
        return price_id;
    }

    public void setPrice_id(Integer price_id) {
        this.price_id = price_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_mm() {
        return name_mm;
    }

    public void setName_mm(String name_mm) {
        this.name_mm = name_mm;
    }

    public String getName_ch() {
        return name_ch;
    }

    public void setName_ch(String name_ch) {
        this.name_ch = name_ch;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
