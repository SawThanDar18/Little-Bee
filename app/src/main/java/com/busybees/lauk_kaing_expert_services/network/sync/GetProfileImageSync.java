package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.ProfileImageModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.ProfileImageObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProfileImageSync {
    @POST()
    Call<ProfileImageModel> getProfileImage(@Url String url, @Body ProfileImageObj profileImageObj);
}
