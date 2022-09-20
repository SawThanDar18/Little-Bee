package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.ProfileImageModel;
import com.busybees.little_bee.data.vos.Users.ProfileImageObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProfileImageSync {
    @POST()
    Call<ProfileImageModel> getProfileImage(@Url String url, @Body ProfileImageObj profileImageObj);
}
