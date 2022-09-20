package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.MyHistory.MyHistoryModel;
import com.busybees.little_bee.data.vos.Users.RequestPhoneObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetMyOrdersHistorySync {
    @POST()
    Call<MyHistoryModel> getMyOrdersHistory(@Url String url, @Body RequestPhoneObject phoneObj);
}
