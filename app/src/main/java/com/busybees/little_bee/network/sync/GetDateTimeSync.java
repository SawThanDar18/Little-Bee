package com.busybees.little_bee.network.sync;


import com.busybees.little_bee.data.models.GetDateTimeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDateTimeSync {
    @GET()
    Call<GetDateTimeModel> getGetTime(@Url String url);
}
