package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.GetProductPriceModel;
import com.busybees.little_bee.data.vos.Home.request_object.ProductsCarryObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetProductPriceSync {

    @POST
    Call<GetProductPriceModel> getProductPrice(@Url String url, @Body ProductsCarryObject productsCarryObject);
}
