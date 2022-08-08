package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.busybees.lauk_kaing_expert_services.Banner.AdvertisementBannerAdapter;
import com.busybees.lauk_kaing_expert_services.activity.SearchActivity;
import com.busybees.lauk_kaing_expert_services.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.PopularServicesVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceNeedVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.SliderVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
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
import com.busybees.lauk_kaing_expert_services.utility.AutoScrollRecyclerView;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private ImageView footerImage;

    private LinearLayout reloadPage;
    private Button reloadBtn;
    private ProgressBar progressBar;

    private TextView productName;

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    private ArrayList<PopularServicesVO> popularServicesVOArrayList = new ArrayList<>();
    private ArrayList<ServiceNeedVO> serviceNeedVOArrayList = new ArrayList<>();

    private UserVO userVO = new UserVO();

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

        footerImage = view.findViewById(R.id.footer);

        reloadPage = view.findViewById(R.id.reload_page);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        progressBar = view.findViewById(R.id.materialLoader);
        productName = view.findViewById(R.id.s_name);

        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);

        if (Utility.isOnline(getContext())) {
            setUpAdapterToRecyclerView();
            reloadPage.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        CallGetAllHomeApi();

        onRecyclerViewClick();
        onClick();

        return  view;
    }

    private void onClick() {

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

    private void initImageSlider(List<SliderVO> slider) {

        List<String> sliderImageList = new ArrayList<>();

        for (int i = 0; i < slider.size(); i++) {
            sliderImageList.add(slider.get(i).getSliderImage());
        }

        List<Integer> adsImageList = new ArrayList<>();
        adsImageList.add(R.drawable.ads);
        advertisementBannerAdapter = new AdvertisementBannerAdapter(getActivity(), adsImageList);

        webBannerAdapter = new WebBannerAdapter(getActivity(), sliderImageList);

        webBannerAdapter.setOnBannerItemClickListener(position -> {

            if (userVO == null) {
                startActivity(new Intent(getActivity().getApplicationContext(), LogInActivity.class));

            } else {

                ProductsCarryObject productsCarryObject = new ProductsCarryObject();
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

        if (Utility.isOnline(getActivity())) {
            serviceProvider.GetHomeCall(ApiConstants.BASE_URL + ApiConstants.GET_ALL_HOME).enqueue(new Callback<GetAllHomeModel>() {

                @Override
                public void onResponse(Call<GetAllHomeModel> call, Response<GetAllHomeModel> response) {

                    initImageSlider(response.body().getData().getSlider());

                    progressBar.setVisibility(View.GONE);
                    reloadPage.setVisibility(View.GONE);

                    setUpAdapterToRecyclerView();

                    availableAdapter = new AvailableAdapter(getActivity(), response.body().getData().getServiceAvailable());
                    recyclerViewAvailable.setAdapter(availableAdapter);
                    availableAdapter.notifyDataSetChanged();
                    serviceAvailableVOArrayList.addAll(response.body().getData().getServiceAvailable());

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