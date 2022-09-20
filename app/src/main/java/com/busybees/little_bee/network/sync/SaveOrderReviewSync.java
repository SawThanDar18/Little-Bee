package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.SaveOrderReview.SaveOrderReviewModel;
import com.busybees.little_bee.data.vos.SaveOrderReview.SaveOrderReviewVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SaveOrderReviewSync {
    @POST()
    Call<SaveOrderReviewModel> getSOReview(@Url String url, @Body SaveOrderReviewVO soReviewObj);
}

