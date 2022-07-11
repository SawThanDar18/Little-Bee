package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.GetUserProfileModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.GetUserProfileObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetUserProfileSync {

    @POST()
    Call<GetUserProfileModel> getUserProfile(@Url String url, @Body GetUserProfileObject getUserProfileObject);
}
