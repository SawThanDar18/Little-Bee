package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.MyOrders.MyOrderModel;
import com.busybees.little_bee.data.vos.Users.RequestPhoneObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetMyOrdersSync {
    @POST()
    Call<MyOrderModel> getMyOrders(@Url String url, @Body RequestPhoneObject phoneObj);
}
