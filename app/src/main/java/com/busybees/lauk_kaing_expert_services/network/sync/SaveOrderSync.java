package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.SaveOrderModel;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.SaveOrderObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SaveOrderSync {

    @POST
    Call<SaveOrderModel> saveOrder(@Url String url, @Body SaveOrderObject saveOrderObject);
}
