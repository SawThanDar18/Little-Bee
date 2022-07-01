package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.data.models.GetProductPriceModel;
import com.busybees.data.vos.Home.request_object.ProductsCarryObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProductPriceSync {

    @POST
    Call<GetProductPriceModel> getProductPrice(@Url String url, @Body ProductsCarryObject productsCarryObject);
}
