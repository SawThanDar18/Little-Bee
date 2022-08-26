package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.MyHistory.MyHistoryModel;
import com.busybees.lauk_kaing_expert_services.data.models.MyOrders.MyOrderModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.RequestPhoneObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetMyOrdersHistorySync {
    @POST()
    Call<MyHistoryModel> getMyOrdersHistory(@Url String url, @Body RequestPhoneObject phoneObj);
}
