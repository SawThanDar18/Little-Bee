package com.busybees.lauk_kaing_expert_services.network.sync;


import com.busybees.lauk_kaing_expert_services.data.models.GetDateTimeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDateTimeSync {
    @GET()
    Call<GetDateTimeModel> getGetTime(@Url String url);
}
