package com.busybees.lauk_kaing_expert_services.network.sync;


import com.busybees.lauk_kaing_expert_services.data.models.AddressModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.DeleteAddressObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DeleteAddressSync {
    @POST()
    Call<AddressModel> getDeleteAddress(@Url String url, @Body DeleteAddressObject deleteAddressObject);
}
