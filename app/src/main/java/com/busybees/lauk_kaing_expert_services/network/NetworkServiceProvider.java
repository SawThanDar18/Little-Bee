package com.busybees.lauk_kaing_expert_services.network;

import android.content.Context;

import com.busybees.data.models.GetAllHomeModel;
import com.busybees.data.models.GetProductPriceModel;
import com.busybees.data.models.LoginModel;
import com.busybees.data.models.ProfileUpdateModel;
import com.busybees.data.models.ResendOtpModel;
import com.busybees.data.models.VerifyModel;
import com.busybees.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.data.vos.Users.LoginObject;
import com.busybees.data.vos.Users.ProfileUpdateObj;
import com.busybees.data.vos.Users.ResendOTPObject;
import com.busybees.data.vos.Users.VerifyObject;
import com.busybees.lauk_kaing_expert_services.network.sync.GetAllHomeSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetLoginSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetProductPriceSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetProfileUpdateSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetResendOtpSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetVerifySync;

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

    public Call<GetProductPriceModel> GetProductPriceCall (String url, ProductsCarryObject productsCarryObject) {
        GetProductPriceSync sync = retrofit.create(GetProductPriceSync.class);
        return sync.getProductPrice(url, productsCarryObject);
    }

    public Call<LoginModel> LoginCall (String url, LoginObject loginObj) {
        GetLoginSync sync=retrofit.create(GetLoginSync.class);
        return sync.getLogin(url, loginObj);
    }

    public Call<VerifyModel> VerifyCall (String url, VerifyObject verifyObj) {
        GetVerifySync sync=retrofit.create(GetVerifySync.class);
        return sync.getVerify(url, verifyObj);
    }

    public Call<ResendOtpModel> ResendOtpCall (String url, ResendOTPObject resendOTPObject) {
        GetResendOtpSync sync=retrofit.create(GetResendOtpSync.class);
        return sync.getResendOtp(url, resendOTPObject);
    }

    public Call<ProfileUpdateModel> ProfileUpdateCall (String url, ProfileUpdateObj updateObj) {
        GetProfileUpdateSync sync=retrofit.create(GetProfileUpdateSync.class);
        return sync.getProfileUpdate(url, updateObj);
    }
}
