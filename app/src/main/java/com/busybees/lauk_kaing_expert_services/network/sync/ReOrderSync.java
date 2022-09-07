package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.ReOrder.ReOrderModel;
import com.busybees.lauk_kaing_expert_services.data.vos.ReOrder.ReOrderVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ReOrderSync {
    @POST()
    Call<ReOrderModel> getReOrder(@Url String url, @Body ReOrderVO reOrderObj);
}
