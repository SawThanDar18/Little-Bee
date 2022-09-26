package com.busybees.little_bee.network;

import android.content.Context;

import com.busybees.little_bee.data.models.AddAddressModel;
import com.busybees.little_bee.data.models.AddToCart.AddToCartModel;
import com.busybees.little_bee.data.models.AddToCart.AddToCartObj;
import com.busybees.little_bee.data.models.AddressModel;
import com.busybees.little_bee.data.models.DeleteUser.GetDeleteUserModel;
import com.busybees.little_bee.data.models.ForceUpdate.ForceUpdateObj;
import com.busybees.little_bee.data.models.ForceUpdate.ForeUpdateModel;
import com.busybees.little_bee.data.models.GetAddressModel;
import com.busybees.little_bee.data.models.GetCart.GetCartModel;
import com.busybees.little_bee.data.models.GetCart.GetCartObj;
import com.busybees.little_bee.data.models.GetDateTimeModel;
import com.busybees.little_bee.data.models.MatchPromoCodeModel;
import com.busybees.little_bee.data.models.MyHistory.MyHistoryModel;
import com.busybees.little_bee.data.models.MyOrders.MyOrderModel;
import com.busybees.little_bee.data.models.Notifications.NotificationsModel;
import com.busybees.little_bee.data.models.ReOrder.ReOrderModel;
import com.busybees.little_bee.data.models.SaveOrder.SaveOrderModel;
import com.busybees.little_bee.data.models.SaveOrder.SaveOrderObject;
import com.busybees.little_bee.data.models.SaveOrderReview.SaveOrderReviewModel;
import com.busybees.little_bee.data.models.SaveToken.SaveTokenModel;
import com.busybees.little_bee.data.models.SaveToken.SaveTokenObj;
import com.busybees.little_bee.data.models.Search.SearchModel;
import com.busybees.little_bee.data.models.UserGuide.UserGuideModel;
import com.busybees.little_bee.data.vos.Address.AddAddresssObject;
import com.busybees.little_bee.data.vos.Address.DeleteAddressObject;
import com.busybees.little_bee.data.vos.Address.EditAddressObject;
import com.busybees.little_bee.data.vos.PromoCOde.MatchPromoCodeObject;
import com.busybees.little_bee.data.vos.ReOrder.ReOrderVO;
import com.busybees.little_bee.data.vos.SaveOrderReview.SaveOrderReviewVO;
import com.busybees.little_bee.data.vos.Search.SearchVO;
import com.busybees.little_bee.data.vos.Users.ProfileImageObj;
import com.busybees.little_bee.data.vos.Users.RequestPhoneObject;
import com.busybees.little_bee.network.sync.AddAddressSync;
import com.busybees.little_bee.network.sync.AddToCartSync;
import com.busybees.little_bee.network.sync.DeleteAddressSync;
import com.busybees.little_bee.network.sync.EditAddressSync;
import com.busybees.little_bee.network.sync.ForceUpdateSync;
import com.busybees.little_bee.network.sync.GetAddressSync;
import com.busybees.little_bee.data.models.GetAllHomeModel;
import com.busybees.little_bee.data.models.GetProductPriceModel;
import com.busybees.little_bee.data.models.GetUserProfileModel;
import com.busybees.little_bee.data.models.LoginModel;
import com.busybees.little_bee.data.models.ProfileUpdateModel;
import com.busybees.little_bee.data.models.RegisterModel;
import com.busybees.little_bee.data.models.ResendOtpModel;
import com.busybees.little_bee.data.models.VerifyModel;
import com.busybees.little_bee.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.little_bee.data.vos.Users.GetUserProfileObject;
import com.busybees.little_bee.data.vos.Users.LoginObject;
import com.busybees.little_bee.data.models.ProfileImageModel;
import com.busybees.little_bee.data.vos.Users.ProfileUpdateObj;
import com.busybees.little_bee.data.vos.Users.RegisterObj;
import com.busybees.little_bee.data.vos.Users.ResendOTPObject;
import com.busybees.little_bee.data.vos.Users.VerifyObject;
import com.busybees.little_bee.network.sync.GetAllHomeSync;
import com.busybees.little_bee.network.sync.GetCartSync;
import com.busybees.little_bee.network.sync.GetDateTimeSync;
import com.busybees.little_bee.network.sync.GetDeleteUserSync;
import com.busybees.little_bee.network.sync.GetLoginSync;
import com.busybees.little_bee.network.sync.GetMyOrdersHistorySync;
import com.busybees.little_bee.network.sync.GetMyOrdersSync;
import com.busybees.little_bee.network.sync.GetProductPriceSync;
import com.busybees.little_bee.network.sync.GetProfileImageSync;
import com.busybees.little_bee.network.sync.GetProfileUpdateSync;
import com.busybees.little_bee.network.sync.GetRegisterSync;
import com.busybees.little_bee.network.sync.GetResendOtpSync;
import com.busybees.little_bee.network.sync.GetUserGuideSync;
import com.busybees.little_bee.network.sync.GetUserProfileSync;
import com.busybees.little_bee.network.sync.GetVerifySync;
import com.busybees.little_bee.network.sync.MatchPromoCodeSync;
import com.busybees.little_bee.network.sync.NotificationSync;
import com.busybees.little_bee.network.sync.ReOrderSync;
import com.busybees.little_bee.network.sync.SaveOrderReviewSync;
import com.busybees.little_bee.network.sync.SaveOrderSync;
import com.busybees.little_bee.network.sync.SaveTokenSync;
import com.busybees.little_bee.network.sync.SearchSync;

import retrofit2.Call;
import retrofit2.Retrofit;

public class NetworkServiceProvider {

    Context context;
    Retrofit retrofit;

    public NetworkServiceProvider(Context context) {
        this.context = context;
        RetrofitFactory factory = new RetrofitFactory();
        retrofit = factory.connector();
    }

    public Call<GetAllHomeModel> GetHomeCall(String url) {
        GetAllHomeSync sync = retrofit.create(GetAllHomeSync.class);
        return sync.getAllHome(url);
    }

    public Call<GetProductPriceModel> GetProductPriceCall(String url, ProductsCarryObject productsCarryObject) {
        GetProductPriceSync sync = retrofit.create(GetProductPriceSync.class);
        return sync.getProductPrice(url, productsCarryObject);
    }

    public Call<RegisterModel> RegisterCall(String url, RegisterObj registerObj) {
        GetRegisterSync sync = retrofit.create(GetRegisterSync.class);
        return sync.getRegister(url, registerObj);
    }

    public Call<LoginModel> LoginCall(String url, LoginObject loginObj) {
        GetLoginSync sync = retrofit.create(GetLoginSync.class);
        return sync.getLogin(url, loginObj);
    }

    public Call<VerifyModel> VerifyCall(String url, VerifyObject verifyObj) {
        GetVerifySync sync = retrofit.create(GetVerifySync.class);
        return sync.getVerify(url, verifyObj);
    }

    public Call<ResendOtpModel> ResendOtpCall(String url, ResendOTPObject resendOTPObject) {
        GetResendOtpSync sync = retrofit.create(GetResendOtpSync.class);
        return sync.getResendOtp(url, resendOTPObject);
    }

    public Call<ProfileUpdateModel> ProfileUpdateCall(String url, ProfileUpdateObj updateObj) {
        GetProfileUpdateSync sync = retrofit.create(GetProfileUpdateSync.class);
        return sync.getProfileUpdate(url, updateObj);
    }

    public Call<GetUserProfileModel> UserProfileCall(String url, GetUserProfileObject getUserProfileObject) {
        GetUserProfileSync sync = retrofit.create(GetUserProfileSync.class);
        return sync.getUserProfile(url, getUserProfileObject);
    }

    public Call<ProfileImageModel> ProfileImageCall(String url, ProfileImageObj profileImageObj) {
        GetProfileImageSync sync = retrofit.create(GetProfileImageSync.class);
        return sync.getProfileImage(url, profileImageObj);
    }

    public Call<GetDateTimeModel> DateTimeCall(String url) {
        GetDateTimeSync sync = retrofit.create(GetDateTimeSync.class);
        return sync.getGetTime(url);
    }

    public Call<GetAddressModel> GetAddressCall(String url, RequestPhoneObject phoneObj) {
        GetAddressSync sync = retrofit.create(GetAddressSync.class);
        return sync.getAddress(url, phoneObj);
    }

    public Call<AddAddressModel> AddAddressCall(String url, AddAddresssObject addressObj) {
        AddAddressSync sync = retrofit.create(AddAddressSync.class);
        return sync.getAddAddress(url, addressObj);
    }

    public Call<AddressModel> DeleteAddressCall(String url, DeleteAddressObject deleteAddressObject) {
        DeleteAddressSync sync = retrofit.create(DeleteAddressSync.class);
        return sync.getDeleteAddress(url, deleteAddressObject);
    }

    public Call<AddressModel> EditAddressCall(String url, EditAddressObject editAddressObject) {
        EditAddressSync sync = retrofit.create(EditAddressSync.class);
        return sync.getEditAddress(url, editAddressObject);
    }

    public Call<GetCartModel> GetCartCall (String url, GetCartObj getCartObj) {
        GetCartSync sync=retrofit.create(GetCartSync.class);
        return sync.getGetCart(url,getCartObj);
    }

    public Call<AddToCartModel> AddToCartCall (String url, AddToCartObj addToCartObj) {
        AddToCartSync sync=retrofit.create(AddToCartSync.class);
        return sync.getAddToCart(url,addToCartObj);
    }

    public Call<MatchPromoCodeModel> MatchPromoCode (String url, MatchPromoCodeObject obj) {
        MatchPromoCodeSync sync=retrofit.create(MatchPromoCodeSync.class);
        return sync.matchPromoCode(url, obj);
    }

    public Call<SaveOrderModel> SaveOrderCall (String url, SaveOrderObject saveOrderObject) {
        SaveOrderSync sync = retrofit.create(SaveOrderSync.class);
        return sync.saveOrder(url, saveOrderObject);
    }

    public Call<MyOrderModel> GetMyOrdersCall (String url, RequestPhoneObject phoneObject) {
        GetMyOrdersSync sync = retrofit.create(GetMyOrdersSync.class);
        return sync.getMyOrders(url, phoneObject);
    }

    public Call<MyHistoryModel> GetMyOrdersHistoryCall (String url, RequestPhoneObject phoneObject) {
        GetMyOrdersHistorySync sync = retrofit.create(GetMyOrdersHistorySync.class);
        return sync.getMyOrdersHistory(url, phoneObject);
    }

    public Call<UserGuideModel> GetUserGuideCall (String url) {
        GetUserGuideSync sync = retrofit.create(GetUserGuideSync.class);
        return sync.getUserGuide(url);
    }

    public Call<SaveTokenModel> GetSaveNotiTokenCall(String url, SaveTokenObj saveTokenObj) {
        SaveTokenSync sync = retrofit.create(SaveTokenSync.class);
        return sync.getSaveToken(url,saveTokenObj);
    }

    public Call<SaveOrderReviewModel> SOReview (String url, SaveOrderReviewVO soReviewObj) {
        SaveOrderReviewSync sync=retrofit.create(SaveOrderReviewSync.class);
        return sync.getSOReview(url, soReviewObj);
    }

    public Call<ReOrderModel> ReOrder (String url, ReOrderVO reOrderObj) {
        ReOrderSync sync=retrofit.create(ReOrderSync.class);
        return sync.getReOrder(url,reOrderObj);
    }

    public Call<NotificationsModel> Notification (String url, RequestPhoneObject phoneObj) {
        NotificationSync sync=retrofit.create(NotificationSync.class);
        return sync.getNoti(url,phoneObj);
    }

    public Call<GetDeleteUserModel> DeleteUserCall (String url, RequestPhoneObject requestPhoneObject) {
        GetDeleteUserSync sync = retrofit.create(GetDeleteUserSync.class);
        return sync.getDeleteUser(url, requestPhoneObject);
    }

    public Call<SearchModel> Search (String url, SearchVO searchObj) {
        SearchSync sync=retrofit.create(SearchSync.class);
        return sync.getSearch(url,searchObj);
    }

    public Call<ForeUpdateModel> ForceUpdate (String url, ForceUpdateObj forceUpdateObj) {
        ForceUpdateSync sync=retrofit.create(ForceUpdateSync.class);
        return sync.getForceUpdate(url,forceUpdateObj);
    }

}
