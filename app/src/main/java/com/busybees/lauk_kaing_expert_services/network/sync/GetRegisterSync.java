package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.RegisterModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RegisterObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetRegisterSync {
    @POST()
    Call<RegisterModel> getRegister(@Url String url, @Body RegisterObj registerObj);
}
