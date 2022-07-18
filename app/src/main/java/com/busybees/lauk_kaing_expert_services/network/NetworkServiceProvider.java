package com.busybees.lauk_kaing_expert_services.network;

import android.content.Context;

import com.busybees.data.models.AddAddressModel;
import com.busybees.data.models.AddressModel;
import com.busybees.data.models.GetAddressModel;
import com.busybees.data.models.GetAllHomeModel;
import com.busybees.data.models.GetDateTimeModel;
import com.busybees.data.models.GetProductPriceModel;
import com.busybees.data.models.GetUserProfileModel;
import com.busybees.data.models.LoginModel;
import com.busybees.data.models.ProfileUpdateModel;
import com.busybees.data.models.RegisterModel;
import com.busybees.data.models.ResendOtpModel;
import com.busybees.data.models.VerifyModel;
import com.busybees.data.vos.Address.AddAddresssObject;
import com.busybees.data.vos.Address.DeleteAddressObject;
import com.busybees.data.vos.Address.EditAddressObject;
import com.busybees.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.data.vos.Users.GetUserProfileObject;
import com.busybees.data.vos.Users.LoginObject;
import com.busybees.data.vos.Users.ProfileUpdateObj;
import com.busybees.data.vos.Users.RegisterObj;
import com.busybees.data.vos.Users.RequestPhoneObject;
import com.busybees.data.vos.Users.ResendOTPObject;
import com.busybees.data.vos.Users.VerifyObject;
import com.busybees.lauk_kaing_expert_services.network.sync.AddAddressSync;
import com.busybees.lauk_kaing_expert_services.network.sync.DeleteAddressSync;
import com.busybees.lauk_kaing_expert_services.network.sync.EditAddressSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetAddressSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetAllHomeSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetDateTimeSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetLoginSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetProductPriceSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetProfileUpdateSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetRegisterSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetResendOtpSync;
import com.busybees.lauk_kaing_expert_services.network.sync.GetUserProfileSync;
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

    public Call<RegisterModel> RegisterCall (String url, RegisterObj registerObj) {
        GetRegisterSync sync=retrofit.create(GetRegisterSync.class);
        return sync.getRegister(url,registerObj);
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

    public Call<GetUserProfileModel> UserProfileCall (String url, GetUserProfileObject getUserProfileObject) {
        GetUserProfileSync sync=retrofit.create(GetUserProfileSync.class);
        return sync.getUserProfile(url, getUserProfileObject);
    }

    public Call<GetDateTimeModel> DateTimeCall(String url) {
        GetDateTimeSync sync=retrofit.create(GetDateTimeSync.class);
        return sync.getGetTime(url);
    }

    public Call<GetAddressModel> GetAddressCall (String url, RequestPhoneObject phoneObj) {
        GetAddressSync sync = retrofit.create(GetAddressSync.class);
        return sync.getAddress(url, phoneObj);
    }

    public Call<AddAddressModel> AddAddressCall(String url, AddAddresssObject addressObj) {
        AddAddressSync sync=retrofit.create(AddAddressSync.class);
        return sync.getAddAddress(url,addressObj);
    }

    public Call<AddressModel> DeleteAddressCall(String url, DeleteAddressObject deleteAddressObject) {
        DeleteAddressSync sync = retrofit.create(DeleteAddressSync.class);
        return sync.getDeleteAddress(url, deleteAddressObject);
    }

    public Call<AddressModel> EditAddressCall (String url, EditAddressObject editAddressObject) {
        EditAddressSync sync=retrofit.create(EditAddressSync.class);
        return sync.getEditAddress(url, editAddressObject);
    }

}
