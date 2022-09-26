package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.ForceUpdate.ForceUpdateObj;
import com.busybees.little_bee.data.models.ForceUpdate.ForeUpdateModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ForceUpdateSync {
    @POST()
    Call<ForeUpdateModel> getForceUpdate(@Url String url, @Body ForceUpdateObj forceUpdateObj);
}
