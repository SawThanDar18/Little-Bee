package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.Search.SearchModel;
import com.busybees.little_bee.data.vos.Search.SearchVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SearchSync {
    @POST()
    Call<SearchModel> getSearch(@Url String url, @Body SearchVO searchObj);
}
