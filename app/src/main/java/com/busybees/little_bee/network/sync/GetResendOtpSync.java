package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.ResendOtpModel;
import com.busybees.little_bee.data.vos.Users.ResendOTPObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetResendOtpSync {
    @POST()
    Call<ResendOtpModel> getResendOtp (@Url String url, @Body ResendOTPObject resendOTPObject);
}
