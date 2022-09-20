package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.GetAllHomeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetAllHomeSync {
    @GET
    Call<GetAllHomeModel> getAllHome(@Url String url);
}
