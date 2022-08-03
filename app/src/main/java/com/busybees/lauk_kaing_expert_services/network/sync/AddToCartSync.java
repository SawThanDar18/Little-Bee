package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartObj;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AddToCartSync {
    @POST()
    Call<AddToCartModel> getAddToCart(@Url String url, @Body AddToCartObj addToCartObj);

    @POST
    Call<AddToCartModel> uploadMultiFile(@Url String url, @Body RequestBody file);
}
