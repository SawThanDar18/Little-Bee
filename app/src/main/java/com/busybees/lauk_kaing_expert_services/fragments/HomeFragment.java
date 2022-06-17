package com.busybees.lauk_kaing_expert_services.fragments;

import android.os.Bundle;

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

import com.busybees.lauk_kaing_expert_services.Banner.BannerLayout;
import com.busybees.lauk_kaing_expert_services.Banner.WebBannerAdapter;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Home.AvailableAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.PopularAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.SymnAdapter;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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
            initImageSlider();
            setUpAdapterToRecyclerView();
            reloadPage.setVisibility(View.GONE);
            //progressBar.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);
        }

        return  view;
    }

    private void initImageSlider() {

        /*List<String> imagelist = new ArrayList<>();
        imagelist.add("https://boostcleaningmelbourne.com.au/wp-content/themes/lz-cleaning-services-pro/images/slider-banner.jpg");
        imagelist.add("https://images.squarespace-cdn.com/content/v1/5585b51ce4b0f2164efaa2b1/1558664884095-D2D42IRVFPZH3J0HFCQN/image-asset.jpeg?format=2500w");
        imagelist.add("https://img.grouponcdn.com/deal/3zq2KMhexQw32bJCKUvyxWaEEEdD/3z-2048x1229/v1/c870x524.jpg");
        imagelist.add("https://www.pccsindia.com/wp-content/uploads/2020/04/commercial-clenaing-6-1-1024x561-1.jpg");
*/
        ArrayList<Integer> ex1 = new ArrayList<>();
        ex1.add(R.drawable.banner_image);
        ex1.add(R.drawable.banner_image);
        ex1.add(R.drawable.banner_image);
        ex1.add(R.drawable.banner_image);

        webBannerAdapter = new WebBannerAdapter(getActivity(), ex1);

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

        availableAdapter = new AvailableAdapter(getActivity(), servicesImageList, serviceName);
        layoutManagerRecyclerAvailable = new GridLayoutManager(getActivity(), 4);
        recyclerViewAvailable.setLayoutManager(layoutManagerRecyclerAvailable);
        recyclerViewAvailable.setAdapter(availableAdapter);
        availableAdapter.notifyDataSetChanged();

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
}