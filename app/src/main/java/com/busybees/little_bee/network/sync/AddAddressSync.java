package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.AddAddressModel;
import com.busybees.little_bee.data.vos.Address.AddAddresssObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface AddAddressSync {

    @POST()
    Call<AddAddressModel> getAddAddress(@Url String url, @Body AddAddresssObject addressObj);
}
