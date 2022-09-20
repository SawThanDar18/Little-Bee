package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.VerifyModel;
import com.busybees.little_bee.data.vos.Users.VerifyObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetVerifySync {
    @POST()
    Call<VerifyModel> getVerify(@Url String url, @Body VerifyObject verifyObj);
}
