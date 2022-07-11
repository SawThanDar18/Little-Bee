package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.VerifyModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.VerifyObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetVerifySync {
    @POST()
    Call<VerifyModel> getVerify(@Url String url, @Body VerifyObject verifyObj);
}
