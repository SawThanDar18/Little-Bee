package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.MatchPromoCodeModel;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.MatchPromoCodeObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface MatchPromoCodeSync {
    @POST
    Call<MatchPromoCodeModel> matchPromoCode(@Url String url, @Body MatchPromoCodeObject object);
}
