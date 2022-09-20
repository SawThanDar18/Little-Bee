package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.models.SaveToken.SaveTokenModel;
import com.busybees.little_bee.data.models.SaveToken.SaveTokenObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SaveTokenSync {
    @POST()
    Call<SaveTokenModel> getSaveToken(@Url String url, @Body SaveTokenObj saveTokenObj);
}
