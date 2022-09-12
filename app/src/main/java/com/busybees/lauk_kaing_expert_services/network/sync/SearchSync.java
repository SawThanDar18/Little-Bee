package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Search.SearchVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SearchSync {
    @POST()
    Call<SearchModel> getSearch(@Url String url, @Body SearchVO searchObj);
}
