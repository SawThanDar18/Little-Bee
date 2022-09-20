package com.busybees.little_bee.data.vos.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllHomeVO {

    @SerializedName("slider")
    @Expose
    private List<SliderVO> slider;

    @SerializedName("popular")
    @Expose
    private List<PopularServicesVO> popularServices;

    @SerializedName("service_need")
    @Expose
    private List<ServiceNeedVO> serviceNeed;

    @SerializedName("availables")
    @Expose
    private List<ServiceAvailableVO> serviceAvailable;

    @SerializedName("ads")
    @Expose
    private List<AdsVO> ads;

    public List<SliderVO> getSlider() {
        return slider;
    }

    public void setSlider(List<SliderVO> slider) {
        this.slider = slider;
    }

    public List<PopularServicesVO> getPopularServices() {
        return popularServices;
    }

    public void setPopularServices(List<PopularServicesVO> popularServices) {
        this.popularServices = popularServices;
    }

    public List<ServiceNeedVO> getServiceNeed() {
        return serviceNeed;
    }

    public void setServiceNeed(List<ServiceNeedVO> serviceNeed) {
        this.serviceNeed = serviceNeed;
    }

    public List<ServiceAvailableVO> getServiceAvailable() {
        return serviceAvailable;
    }

    public void setServiceAvailable(List<ServiceAvailableVO> serviceAvailable) {
        this.serviceAvailable = serviceAvailable;
    }

    public List<AdsVO> getAds() {
        return ads;
    }

    public void setAds(List<AdsVO> ads) {
        this.ads = ads;
    }
}
