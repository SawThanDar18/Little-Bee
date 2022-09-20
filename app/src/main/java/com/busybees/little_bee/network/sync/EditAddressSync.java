package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.AddressModel;
import com.busybees.little_bee.data.vos.Address.EditAddressObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface EditAddressSync {
    @POST()
    Call<AddressModel> getEditAddress(@Url String url, @Body EditAddressObject editAddressObject);
}
