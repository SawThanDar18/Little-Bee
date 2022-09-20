package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.ProfileUpdateModel;
import com.busybees.little_bee.data.vos.Users.ProfileUpdateObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProfileUpdateSync {

    @POST()
    Call<ProfileUpdateModel> getProfileUpdate(@Url String url, @Body ProfileUpdateObj updateObj);
}
