package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.GetCart.GetCartModel;
import com.busybees.little_bee.data.models.GetCart.GetCartObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetCartSync {
    @POST()
    Call<GetCartModel> getGetCart(@Url String url, @Body GetCartObj getCartObj);
}
