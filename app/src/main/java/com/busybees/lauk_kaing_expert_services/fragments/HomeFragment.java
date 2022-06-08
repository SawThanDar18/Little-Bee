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

        List<String> imagelist = new ArrayList<>();
        imagelist.add("https://boostcleaningmelbourne.com.au/wp-content/themes/lz-cleaning-services-pro/images/slider-banner.jpg");
        imagelist.add("https://boostcleaningmelbourne.com.au/wp-content/themes/lz-cleaning-services-pro/images/slider-banner.jpg");
        imagelist.add("https://boostcleaningmelbourne.com.au/wp-content/themes/lz-cleaning-services-pro/images/slider-banner.jpg");
        imagelist.add("https://boostcleaningmelbourne.com.au/wp-content/themes/lz-cleaning-services-pro/images/slider-banner.jpg");

        webBannerAdapter = new WebBannerAdapter(getActivity(), imagelist);

        banner.setAdapter(webBannerAdapter);
        banner.setAutoPlaying(true);

    }

    private void setUpAdapterToRecyclerView() {
        availableAdapter = new AvailableAdapter(getActivity());
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