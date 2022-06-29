package com.busybees.lauk_kaing_expert_services.network;

import android.content.Context;

import com.busybees.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.network.sync.GetAllHomeSync;

import retrofit2.Call;
import retrofit2.Retrofit;

public class NetworkServiceProvider {

    Context context;
    Retrofit retrofit;

    public NetworkServiceProvider(Context context) {
        this.context = context;
        RetrofitFactory factory=new RetrofitFactory();
        retrofit = factory.connector();
    }

    public Call<GetAllHomeModel> GetHomeCall (String url) {
        GetAllHomeSync sync = retrofit.create(GetAllHomeSync.class);
        return sync.getAllHome(url);
    }
}
