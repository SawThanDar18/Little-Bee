package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.UserGuide.UserGuideModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetUserGuideSync {
    @GET
    Call<UserGuideModel> getUserGuide(@Url String url);
}