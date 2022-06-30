package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.data.models.GetAllHomeModel;
import com.busybees.data.vos.Home.SliderVO;
import com.busybees.lauk_kaing_expert_services.Banner.BannerLayout;
import com.busybees.lauk_kaing_expert_services.Banner.WebBannerAdapter;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Home.AvailableAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.PopularAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.SymnAdapter;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private NetworkServiceProvider serviceProvider;

    private WebBannerAdapter webBannerAdapter;
    private BannerLayout banner;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        serviceProvider = new NetworkServiceProvider(getActivity());

        banner = view.findViewById(R.id.banner_view);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        recyclerViewAvailable = view.findViewById(R.id.recycle_available);
        recyclerViewPopular = view.findViewById(R.id.recycle_popular);
        recyclerViewServiceYMN = view.findViewById(R.id.recycle_symn);

        footerImage = view.findViewById(R.id.footer);

        reloadPage = view.findViewById(R.id.reload_page);
        reloadBtn = view.findViewById(R.id.btn_reload_page);
        progressBar = view.findViewById(R.id.materialLoader);

        if (Utility.isOnline(getContext())) {
            setUpAdapterToRecyclerView();
            reloadPage.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            footerImage.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            footerImage.setVisibility(View.GONE);
        }

        CallGetAllHomeApi();

        onRecyclerViewClick();
        onClick();

        return  view;
    }

    private void onClick() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            CallGetAllHomeApi();
            swipeRefreshLayout.setRefreshing(false);
        });

        reloadPage.setOnClickListener(v -> {
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
                startActivity(new Intent(getContext(), ProductActivity.class));
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

        webBannerAdapter = new WebBannerAdapter(getActivity(), sliderImageList);

        banner.setAdapter(webBannerAdapter);
        banner.setAutoPlaying(true);

    }

    private void setUpAdapterToRecyclerView() {
        ArrayList<Integer> servicesImageList = new ArrayList<>();
        servicesImageList.add(R.drawable.icon_cleaning__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_general_workers_hiring__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_air_con__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_plumbing__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_electrical__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_laundry__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_cctv__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_painting__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_ceiling__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_upholstery__1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_stone_care__scrubbing___polishing___1200_x_1200_px_);
        servicesImageList.add(R.drawable.ic_pest_control__1200_x_1200_px_);

        ArrayList<String> serviceName = new ArrayList<>();
        serviceName.add("Cleaning");
        serviceName.add("General Worker Hiring");
        serviceName.add("Air-Con");
        serviceName.add("Plumbing");
        serviceName.add("Electrical");
        serviceName.add("Laundry");
        serviceName.add("CCTV");
        serviceName.add("Painting");
        serviceName.add("Ceiling");
        serviceName.add("Upholstery");
        serviceName.add("Stone Care (Scrubbing & Polishing)");
        serviceName.add("Pest Control");

        layoutManagerRecyclerAvailable = new GridLayoutManager(getActivity(), 4);
        recyclerViewAvailable.setLayoutManager(layoutManagerRecyclerAvailable);

        popularAdapter = new PopularAdapter(getActivity());
        layoutManagerRecyclerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopular.setLayoutManager(layoutManagerRecyclerPopular);
        recyclerViewPopular.setAdapter(popularAdapter);
        popularAdapter.notifyDataSetChanged();

        symnAdapter = new SymnAdapter(getActivity());
        layoutManagerRecyclerServiceYMN = new GridLayoutManager(getActivity(), 2);
        recyclerViewServiceYMN.setLayoutManager(layoutManagerRecyclerServiceYMN);
        recyclerViewServiceYMN.setAdapter(symnAdapter);
        symnAdapter.notifyDataSetChanged();
    }

    private void CallGetAllHomeApi() {

        if (Utility.isOnline(getActivity())) {
            serviceProvider.GetHomeCall(ApiConstants.BASE_URL + ApiConstants.GET_ALL_HOME).enqueue(new Callback<GetAllHomeModel>() {

                @Override
                public void onResponse(Call<GetAllHomeModel> call, Response<GetAllHomeModel> response) {

                    initImageSlider(response.body().getData().getSlider());

                    progressBar.setVisibility(View.GONE);
                    reloadPage.setVisibility(View.GONE);

                    availableAdapter = new AvailableAdapter(getActivity(), response.body().getData().getServiceAvailable());
                    recyclerViewAvailable.setAdapter(availableAdapter);
                    availableAdapter.notifyDataSetChanged();
                    /*

                    popularAdapter = new PopularAdapter(getActivity(), response.body().getData().getPopular());
                    recyclePopular.setAdapter(popularAdapter);
                    popularAdapter.notifyDataSetChanged();
                    popularModels.addAll(response.body().getData().getPopular());

                    symnAdapter = new SymnAdapter(getActivity(), response.body().getData().getSymn());
                    layoutManager_symn = new GridLayoutManager(getActivity(), 2);
                    recycleSymn.setLayoutManager(layoutManager_symn);
                    recycleSymn.setAdapter(symnAdapter);
                    symnAdapter.notifyDataSetChanged();
                    symnModels.addAll(response.body().getData().getSymn());

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.drawer_logo);
                    requestOptions.error(R.drawable.drawer_logo);
                    requestOptions.transforms(new CenterCrop());

                    Glide.with(getActivity())
                            .load(response.body().getData().getFooter())
                            .apply(requestOptions)
                            .into(imgfooter);

                    availableAdapter = new AvailableAdapter(getActivity(), response.body().getData().getAvailable());
                    layoutManager_available = new GridLayoutManager(getActivity(), 4);
                    recycleAvailable.setLayoutManager(layoutManager_available);
                    recycleAvailable.setAdapter(availableAdapter);
                    availableAdapter.notifyDataSetChanged();
                    avaliabledata.addAll(response.body().getData().getAvailable());

                    if (response.body().getData().getMore_services() != null) {
                        for (int i = 0; i < response.body().getData().getMore_services().size(); i++) {

                            productlist.addAll(response.body().getData().getMore_services().get(i).getProducts());

                            if ((response.body().getData().getMore_services().size() - 1) == i) {
                                for (int j = 0; j < productlist.size(); j++) {
                                    if (productlist.get(j).getSubProducts() != null) {
                                        subproductlist.addAll(productlist.get(j).getSubProducts());

                                    }
                                }
                            }

                        }
                    }
*/

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
}