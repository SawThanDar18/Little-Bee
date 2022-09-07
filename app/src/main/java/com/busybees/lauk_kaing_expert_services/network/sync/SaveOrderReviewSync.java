package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.SaveOrderReview.SaveOrderReviewModel;
import com.busybees.lauk_kaing_expert_services.data.vos.SaveOrderReview.SaveOrderReviewVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SaveOrderReviewSync {
    @POST()
    Call<SaveOrderReviewModel> getSOReview(@Url String url, @Body SaveOrderReviewVO soReviewObj);
}

