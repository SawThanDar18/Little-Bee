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
}
