package com.busybees.lauk_kaing_expert_services.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.busybees.lauk_kaing_expert_services.Banner.AdvertisementBannerAdapter;
import com.busybees.lauk_kaing_expert_services.Dialog.DialogCall;
import com.busybees.lauk_kaing_expert_services.activity.NotificationActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProfileActivity;
import com.busybees.lauk_kaing_expert_services.activity.SearchActivity;
import com.busybees.lauk_kaing_expert_services.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetUserProfileModel;
import com.busybees.lauk_kaing_expert_services.data.models.SaveToken.SaveTokenModel;
import com.busybees.lauk_kaing_expert_services.data.models.SaveToken.SaveTokenObj;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.AdsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.PopularServicesVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceNeedVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.SliderVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.GetUserProfileObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.Banner.BannerLayout;
import com.busybees.lauk_kaing_expert_services.Banner.WebBannerAdapter;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.LogInActivity;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.activity.ServiceDetailActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Home.AvailableAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.PopularAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.SymnAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.myatminsoe.mdetect.MDetect;
import me.pushy.sdk.Pushy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HomeFragment extends Fragment {

    private NetworkServiceProvider serviceProvider;

    private ConstraintLayout searchLayout;

    private WebBannerAdapter webBannerAdapter;
    private AdvertisementBannerAdapter advertisementBannerAdapter;
    private BannerLayout banner, bannerAdvertisement;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerViewAvailable, recyclerViewPopular, recyclerViewServiceYMN;
    private RecyclerView.LayoutManager layoutManagerRecyclerAvailable, layoutManagerRecyclerPopular, layoutManagerRecyclerServiceYMN;

    private AvailableAdapter availableAdapter;
    private PopularAdapter popularAdapter;
    private SymnAdapter symnAdapter;

    private ImageView phoneCall, notification;
    private CircleImageView profile;

    private LinearLayout reloadPage;
    private Button reloadBtn;
    private ProgressBar progressBar;

    private TextView productName, popularTxt;

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    private ArrayList<PopularServicesVO> popularServicesVOArrayList = new ArrayList<>();
    private ArrayList<ServiceNeedVO> serviceNeedVOArrayList = new ArrayList<>();

    private ArrayList<AdsVO> adsVO = new ArrayList<>();

    private UserVO userVO = new UserVO();

    private String profileUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        serviceProvider = new NetworkServiceProvider(getActivity());
        userVO = Utility.query_UserProfile(getActivity());

        searchLayout = view.findViewById(R.id.search_layout);

        banner = view.findViewById(R.id.banner_view);
        bannerAdvertisement = view.findViewById(R.id.banner_view_advertisement);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        recyclerViewAvailable = view.findViewById(R.id.recycle_available);
        recyclerViewPopular = view.findViewById(R.id.recycle_popular);
        recyclerViewServiceYMN = view.findViewById(R.id.recycle_symn);

        notification = view.findViewById(R.id.notification_btn);
        phoneCall = view.findViewById(R.id.phone_call);
        profile = view.findViewById(R.id.profile);

        reloadPage = view.findViewById(R.id.reload_page);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        progressBar = view.findViewById(R.id.materialLoader);
        productName = view.findViewById(R.id.s_name);
        popularTxt = view.findViewById(R.id.popular_txt);

        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);

        if (Utility.isOnline(getContext())) {
            setUpAdapterToRecyclerView();
            reloadPage.setVisibility(View.GONE);
            popularTxt.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            CallGetAllHomeApi();
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            popularTxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        CallGetAllHomeApi();

        onRecyclerViewClick();
        onClick();

        if (userVO != null) {
            GetUserProfileObject getUserProfileObject = new GetUserProfileObject();
            getUserProfileObject.setPhone(userVO.getPhone());
            CallUserProfile(getUserProfileObject);

            Pushy();
        }

        return  view;
    }

    private void Pushy(){
        if (getDeviceToken() == null) {
            new RegisterForPushNotificationsAsync().execute();
        }
        else {
            Pushy.listen(getContext());
            updateUI();


        }

    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, Exception> {

        public RegisterForPushNotificationsAsync() {

        }

        @SuppressLint("TimberArgCount")
        @Override
        protected Exception doInBackground(String... params) {
            try {
                String deviceToken = Pushy.register(getContext());

                saveDeviceToken(deviceToken);
                Timber.d("deviceToken", deviceToken);

            }
            catch (Exception exc) {
                return exc;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            if (getActivity().isFinishing()) {
                return;
            }

            if (exc != null) {
                Log.e("Pushy", "Registration failed: " + exc.getMessage());

                new AlertDialog.Builder(getContext()).setTitle(R.string.registrationError)
                        .setMessage(exc.getMessage())
                        .setPositiveButton(R.string.ok, null)
                        .create()
                        .show();
            }

            updateUI();
        }
    }

    private void updateUI() {
        String deviceToken = getDeviceToken();

        if (deviceToken == null) {

            return;
        }

        Log.e("PushyToken>>>",deviceToken);
        Log.e("DeviceId>>>", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));


        if (userVO != null || deviceToken != null){
            SaveTokenObj tokenObj=new SaveTokenObj();
            tokenObj.setPhone(userVO.getPhone());
            tokenObj.setToken(deviceToken);
            CallSaveToken(tokenObj);
        }

    }

    public void CallSaveToken(SaveTokenObj obj) {

        if (Utility.isOnline(getActivity())){
            serviceProvider.GetSaveNotiTokenCall(ApiConstants.BASE_URL + ApiConstants.GET_SAVE_NOTI_TOKEN, obj).enqueue(new Callback<SaveTokenModel>() {
                @Override
                public void onResponse(Call<SaveTokenModel> call, Response<SaveTokenModel> response) {


                }
                @Override
                public void onFailure(Call<SaveTokenModel> call, Throwable t) {

                }
            });
        }else {
            Utility.showToast(getContext(), getString(R.string.no_internet));
        }

    }

    private void saveDeviceToken(String deviceToken) {
        getSharedPreferences().edit().putString("deviceToken", deviceToken).commit();
    }

    private String getDeviceToken() {
        return getSharedPreferences().getString("deviceToken", null);
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    private void CallUserProfile(GetUserProfileObject getUserProfileObject) {
        if (Utility.isOnline(getContext())) {
            progressBar.setVisibility(View.VISIBLE);

            serviceProvider.UserProfileCall(ApiConstants.BASE_URL + ApiConstants.GET_USER_PROFILE, getUserProfileObject).enqueue(new Callback<GetUserProfileModel>() {
                @Override
                public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {

                    if (response.body().getError() == true) {
                        Utility.showToast(getContext(), response.body().getMessage());
                    } else if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);

                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.loader_circle_shape);
                        requestOptions.error(R.drawable.loader_circle_shape);

                        profileUrl = response.body().getData().getImage();

                        if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {

                            Glide.with(getContext())
                                    .load(profileUrl)
                                    .apply(requestOptions)
                                    .listener(new RequestListener<Drawable>() {

                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    }).into(profile);
                        }else{

                            Glide.with(getContext())
                                    .load(R.drawable.profile_default_image)
                                    .apply(requestOptions)
                                    .into(profile);

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserProfileModel> call, Throwable t) {
                    Utility.showToast(getContext(), t.getMessage());

                }
            });
        } else {
            Utility.showToast(getContext(), getString(R.string.no_internet));
        }
    }

    private void onClick() {

        phoneCall.setOnClickListener(v -> {
            String phone="09684096773";
            DialogCall dialogCall1=new DialogCall(phone);
            dialogCall1.show(getActivity().getSupportFragmentManager(), "");
        });

        searchLayout.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class)));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            CallGetAllHomeApi();
            swipeRefreshLayout.setRefreshing(false);
        });

        reloadBtn.setOnClickListener(v -> {
            if (Utility.isOnline(getActivity())) {
                CallGetAllHomeApi();
            } else {
                Utility.showToast(getActivity(), getString(R.string.no_internet));
            }
        });

        profile.setOnClickListener(v -> {
            if (userVO != null) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            } else {
                startActivity(new Intent(getContext(), LogInActivity.class));
            }
        });

        notification.setOnClickListener(v -> startActivity(new Intent(getContext(), NotificationActivity.class)));
    }

    private void onRecyclerViewClick() {

        recyclerViewAvailable.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewAvailable, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (userVO == null) {
                    startActivity(new Intent(getContext(), LogInActivity.class));
                } else {
                    if (serviceAvailableVOArrayList.get(position).getStep() == 1) {

                        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
                        productsCarryObject.setPhone(userVO.getPhone());
                        productsCarryObject.setServiceId(serviceAvailableVOArrayList.get(position).getServiceId());
                        productsCarryObject.setStep(serviceAvailableVOArrayList.get(position).getStep());

                        if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                            if ( MDetect.INSTANCE.isUnicode()){

                                Utility.addFontSuHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());

                            } else  {

                                Utility.changeFontUni2ZgHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());
                            }
                        } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                            productName.setText(serviceAvailableVOArrayList.get(position).getNameCh());
                        } else {
                            productName.setText(serviceAvailableVOArrayList.get(position).getName());
                        }

                        Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                        intent.putExtra("product_title", productName.getText().toString());
                        intent.putExtra("product_detail_data", productsCarryObject);
                        startActivity(intent);


                    } else if (serviceAvailableVOArrayList.get(position).getStep() == 2) {
                        ArrayList<ProductsVO> productsVOS = new ArrayList<>();
                        productsVOS.clear();

                        if (productsVOArrayList.size() != 0) {
                            for (int i = 0; i < productsVOArrayList.size(); i++) {

                                if (productsVOArrayList.get(i).getServiceId().equals(serviceAvailableVOArrayList.get(position).getServiceId())) {

                                    ProductsVO productsVO = new ProductsVO();
                                    productsVO.setServiceId(productsVOArrayList.get(i).getServiceId());
                                    productsVO.setProductId(productsVOArrayList.get(i).getProductId());
                                    productsVO.setStep(productsVOArrayList.get(i).getStep());
                                    productsVO.setName(productsVOArrayList.get(i).getName());
                                    productsVO.setNameMm(productsVOArrayList.get(i).getNameMm());
                                    productsVO.setNameCh(productsVOArrayList.get(i).getNameCh());
                                    productsVO.setProductImage(productsVOArrayList.get(i).getProductImage());
                                    productsVO.setSubProducts(productsVOArrayList.get(i).getSubProducts());
                                    productsVOS.add(productsVO);
                                }
                            }

                            if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                                if ( MDetect.INSTANCE.isUnicode()){

                                    Utility.addFontSuHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());

                                } else  {

                                    Utility.changeFontUni2ZgHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());
                                }
                            } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                                productName.setText(serviceAvailableVOArrayList.get(position).getNameCh());
                            } else {
                                productName.setText(serviceAvailableVOArrayList.get(position).getName());
                            }

                            if (productsVOS.size() != 0) {
                                Intent intent = new Intent(getActivity(), ProductActivity.class);
                                intent.putExtra("product_title", productName.getText().toString());
                                intent.putExtra("sub_product", subProductsVOArrayList);
                                intent.putExtra("product", productsVOS);
                                Log.e("pstep2>>>", String.valueOf(productsVOS.size()));
                                startActivity(intent);

                            } else if (productsVOS.size() == 0) {
                                Utility.showToast(getActivity(), "Coming Soon");
                            }
                        }
                    } else {
                        ArrayList<ProductsVO> productsVOS = new ArrayList<>();
                        productsVOS.clear();

                        if (productsVOArrayList.size() != 0) {
                            for (int i = 0; i < productsVOArrayList.size(); i++) {

                                if (productsVOArrayList.get(i).getServiceId().equals(serviceAvailableVOArrayList.get(position).getServiceId())) {

                                    ProductsVO productsVO = new ProductsVO();
                                    productsVO.setServiceId(productsVOArrayList.get(i).getServiceId());
                                    productsVO.setProductId(productsVOArrayList.get(i).getProductId());
                                    productsVO.setStep(productsVOArrayList.get(i).getStep());
                                    productsVO.setName(productsVOArrayList.get(i).getName());
                                    productsVO.setNameMm(productsVOArrayList.get(i).getNameMm());
                                    productsVO.setNameCh(productsVOArrayList.get(i).getNameCh());
                                    productsVO.setProductImage(productsVOArrayList.get(i).getProductImage());
                                    productsVO.setSubProducts(productsVOArrayList.get(i).getSubProducts());
                                    productsVOS.add(productsVO);
                                    Log.e("pstep3>>>", String.valueOf(productsVOS.size()));

                                }
                            }

                            if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                                if ( MDetect.INSTANCE.isUnicode()){

                                    Utility.addFontSuHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());

                                } else  {

                                    Utility.changeFontUni2ZgHome(productName, serviceAvailableVOArrayList.get(position).getNameMm());
                                }
                            } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                                productName.setText(serviceAvailableVOArrayList.get(position).getNameCh());
                            } else {
                                productName.setText(serviceAvailableVOArrayList.get(position).getName());
                            }

                            if (productsVOS.size() != 0) {
                                Intent intent = new Intent(getActivity(), ProductActivity.class);
                                intent.putExtra("product_title", productName.getText().toString());
                                intent.putExtra("sub_product", subProductsVOArrayList);
                                intent.putExtra("product", productsVOS);
                                startActivity(intent);

                            } else if (productsVOS.size() == 0) {
                                Utility.showToast(getActivity(), "Coming Soon");
                            }
                        }
                    }
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        recyclerViewPopular.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewPopular, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (userVO == null) {
                    startActivity(new Intent(getContext(), LogInActivity.class));
                } else {
                    ProductsCarryObject productsCarryObject = new ProductsCarryObject();
                    productsCarryObject.setPhone(userVO.getPhone());
                    productsCarryObject.setServiceId(popularServicesVOArrayList.get(position).getServiceId());
                    productsCarryObject.setProductId(popularServicesVOArrayList.get(position).getProductId());
                    productsCarryObject.setSubProductId(popularServicesVOArrayList.get(position).getSubProductId());
                    productsCarryObject.setStep(popularServicesVOArrayList.get(position).getStep());

                    if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                        if ( MDetect.INSTANCE.isUnicode()){

                            Utility.addFontSuHome(productName, popularServicesVOArrayList.get(position).getNameMm());

                        } else  {

                            Utility.changeFontUni2ZgHome(productName, popularServicesVOArrayList.get(position).getNameMm());
                        }
                    } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                        productName.setText(popularServicesVOArrayList.get(position).getNameCh());
                    } else {
                        productName.setText(popularServicesVOArrayList.get(position).getName());
                    }

                    Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                    intent.putExtra("product_title", productName.getText().toString());
                    intent.putExtra("product_detail_data", productsCarryObject);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        recyclerViewServiceYMN.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewServiceYMN, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (userVO == null) {
                    startActivity(new Intent(getContext(), LogInActivity.class));
                } else {
                    ProductsCarryObject productsCarryObject = new ProductsCarryObject();
                    productsCarryObject.setPhone(userVO.getPhone());
                    productsCarryObject.setServiceId(serviceNeedVOArrayList.get(position).getServiceId());
                    productsCarryObject.setProductId(serviceNeedVOArrayList.get(position).getProductId());
                    productsCarryObject.setSubProductId(serviceNeedVOArrayList.get(position).getSubProductId());
                    productsCarryObject.setStep(serviceNeedVOArrayList.get(position).getStep());

                    if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                        if ( MDetect.INSTANCE.isUnicode()){

                            Utility.addFontSuHome(productName, serviceNeedVOArrayList.get(position).getNameMm());

                        } else  {

                            Utility.changeFontUni2ZgHome(productName, serviceNeedVOArrayList.get(position).getNameMm());
                        }
                    } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                        productName.setText(serviceNeedVOArrayList.get(position).getNameCh());
                    } else {
                        productName.setText(serviceNeedVOArrayList.get(position).getName());
                    }

                    Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                    intent.putExtra("product_title", productName.getText().toString());
                    intent.putExtra("product_detail_data", productsCarryObject);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void initImageSlider(List<SliderVO> slider, List<AdsVO> advertisement) {

        List<String> sliderImageList = new ArrayList<>();

        for (int i = 0; i < slider.size(); i++) {
            sliderImageList.add(slider.get(i).getSliderImage());
        }

        List<String> adsImageList = new ArrayList<>();

        for (int i = 0; i < advertisement.size(); i++) {
            adsImageList.add(advertisement.get(i).getImage());
        }

        webBannerAdapter = new WebBannerAdapter(getActivity(), sliderImageList);

        webBannerAdapter.setOnBannerItemClickListener(position -> {

            if (userVO == null) {
                startActivity(new Intent(getActivity().getApplicationContext(), LogInActivity.class));

            } else {

                ProductsCarryObject productsCarryObject = new ProductsCarryObject();
                productsCarryObject.setPhone(userVO.getPhone());
                productsCarryObject.setServiceId(slider.get(position).getServiceId());
                productsCarryObject.setProductId(slider.get(position).getProductId());
                productsCarryObject.setSubProductId(slider.get(position).getSubProductId());
                productsCarryObject.setStep(slider.get(position).getStep());

                if (checkLng(getActivity()).equalsIgnoreCase("it") || checkLng(getActivity()).equalsIgnoreCase("fr")){
                    if ( MDetect.INSTANCE.isUnicode()){

                        Utility.addFontSuHome(productName, slider.get(position).getNameMm());

                    } else  {

                        Utility.changeFontUni2ZgHome(productName, slider.get(position).getNameMm());
                    }
                } else if (checkLng(getActivity()).equalsIgnoreCase("zh")) {
                    productName.setText(slider.get(position).getNameCh());
                } else {
                    productName.setText(slider.get(position).getName());
                }

                Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
                intent.putExtra("product_title", productName.getText().toString());
                intent.putExtra("product_detail_data", productsCarryObject);
                startActivity(intent);

            }
        });

        banner.setAdapter(webBannerAdapter);
        banner.setAutoPlaying(true);

        advertisementBannerAdapter = new AdvertisementBannerAdapter(getActivity(), adsImageList);

        advertisementBannerAdapter.setOnBannerItemClickListener(position -> {

            if (userVO == null) {
                startActivity(new Intent(getActivity().getApplicationContext(), LogInActivity.class));

            } else {

                if (adsVO != null) {
                    if (adsVO.get(position).getLink() != null) {
                        String adsLink = adsVO.get(position).getLink();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(adsLink));
                        startActivity(intent);
                    }
                }

            }
        });

        bannerAdvertisement.setAdapter(advertisementBannerAdapter);
        bannerAdvertisement.setAutoPlaying(true);

    }

    private void setUpAdapterToRecyclerView() {

        //layoutManagerRecyclerAvailable = new GridLayoutManager(getActivity(), 4);
        layoutManagerRecyclerAvailable = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAvailable.setLayoutManager(layoutManagerRecyclerAvailable);

        layoutManagerRecyclerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopular.setLayoutManager(layoutManagerRecyclerPopular);

        layoutManagerRecyclerServiceYMN = new GridLayoutManager(getActivity(), 2);
        recyclerViewServiceYMN.setLayoutManager(layoutManagerRecyclerServiceYMN);
    }

    private void CallGetAllHomeApi() {

        progressBar.setVisibility(View.VISIBLE);

        if (Utility.isOnline(getActivity())) {
            serviceProvider.GetHomeCall(ApiConstants.BASE_URL + ApiConstants.GET_ALL_HOME).enqueue(new Callback<GetAllHomeModel>() {

                @Override
                public void onResponse(Call<GetAllHomeModel> call, Response<GetAllHomeModel> response) {

                    if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);
                        initImageSlider(response.body().getData().getSlider(), response.body().getData().getAds());

                        adsVO.clear();
                        adsVO.addAll(response.body().getData().getAds());

                        progressBar.setVisibility(View.GONE);
                        reloadPage.setVisibility(View.GONE);
                        popularTxt.setVisibility(View.VISIBLE);

                        setUpAdapterToRecyclerView();

                        availableAdapter = new AvailableAdapter(getActivity(), response.body().getData().getServiceAvailable());
                        recyclerViewAvailable.setAdapter(availableAdapter);
                        availableAdapter.notifyDataSetChanged();
                        serviceAvailableVOArrayList.addAll(response.body().getData().getServiceAvailable());

                        productsVOArrayList.clear();
                        subProductsVOArrayList.clear();

                        if (response.body().getData().getServiceAvailable() != null) {
                            for (int i = 0; i < response.body().getData().getServiceAvailable().size(); i++) {

                                productsVOArrayList.addAll(response.body().getData().getServiceAvailable().get(i).getProducts());

                                if ((response.body().getData().getServiceAvailable().size() - 1) == i) {
                                    for (int j = 0; j < productsVOArrayList.size(); j++) {
                                        if (productsVOArrayList.get(j).getSubProducts() != null) {
                                            subProductsVOArrayList.addAll(productsVOArrayList.get(j).getSubProducts());
                                        }
                                    }
                                }

                            }
                        }

                        popularAdapter = new PopularAdapter(getActivity(), response.body().getData().getPopularServices());
                        recyclerViewPopular.setAdapter(popularAdapter);
                        popularAdapter.notifyDataSetChanged();
                        popularServicesVOArrayList.addAll(response.body().getData().getPopularServices());

                        symnAdapter = new SymnAdapter(getActivity(), response.body().getData().getServiceNeed());
                        recyclerViewServiceYMN.setAdapter(symnAdapter);
                        symnAdapter.notifyDataSetChanged();
                        serviceNeedVOArrayList.addAll(response.body().getData().getServiceNeed());

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<GetAllHomeModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(getActivity(), getString(R.string.no_internet));
        }
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getEventBusCart(String home) {

        userVO = Utility.query_UserProfile(getActivity());

    }
}