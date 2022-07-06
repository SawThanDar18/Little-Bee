package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.data.models.ProfileUpdateModel;
import com.busybees.data.vos.Users.ProfileUpdateObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProfileUpdateSync {

    @POST()
    Call<ProfileUpdateModel> getProfileUpdate(@Url String url, @Body ProfileUpdateObj updateObj);
}
