package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.UserGuide.UserGuideModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetUserGuideSync {
    @GET
    Call<UserGuideModel> getUserGuide(@Url String url);
}