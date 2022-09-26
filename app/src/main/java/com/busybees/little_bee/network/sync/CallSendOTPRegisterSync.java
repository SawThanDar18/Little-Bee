package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.VerifyModel;
import com.busybees.little_bee.data.vos.Users.RegisterObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CallSendOTPRegisterSync {

    @POST
    Call<VerifyModel> callSendOTP(@Url String url, @Body RegisterObj object);

}
