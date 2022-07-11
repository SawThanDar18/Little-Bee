package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.LoginModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.LoginObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetLoginSync {
    @POST()
    Call<LoginModel> getLogin(@Url String url, @Body LoginObject loginObj);
}
