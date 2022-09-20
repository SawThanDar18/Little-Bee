package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.LoginModel;
import com.busybees.little_bee.data.vos.Users.LoginObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetLoginSync {
    @POST()
    Call<LoginModel> getLogin(@Url String url, @Body LoginObject loginObj);
}
