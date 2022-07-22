package com.busybees.lauk_kaing_expert_services.network.sync;

import com.busybees.lauk_kaing_expert_services.data.models.AddressModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.EditAddressObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface EditAddressSync {
    @POST()
    Call<AddressModel> getEditAddress(@Url String url, @Body EditAddressObject editAddressObject);
}
