package com.busybees.lauk_kaing_expert_services.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.AddressActivity;
import com.busybees.lauk_kaing_expert_services.adapters.Carts.CartsListAdapter;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class CartsFragment extends Fragment {

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RelativeLayout cartTimeline, logInView, noLogInView, noServicesView;
    private LinearLayout continueLayout, reloadPage;

    private RecyclerView cartsRecyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private CartsListAdapter cartsListAdapter;

    private ImageView homePageView;
    private TextView cartCountText;
    private Button goToLogInBtn, goToServicesBtn, reloadBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carts, container, false);

        progressBar = view.findViewById(R.id.materialLoader);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        cartTimeline = view.findViewById(R.id.cart_timeline);
        logInView = view.findViewById(R.id.loginView);
        noLogInView = view.findViewById(R.id.no_login_view);
        noServicesView = view.findViewById(R.id.no_service_view);
        continueLayout = view.findViewById(R.id.continue_layout);
        reloadPage = view.findViewById(R.id.reload_page);
        homePageView = view.findViewById(R.id.home_page_btn);
        cartsRecyclerView = view.findViewById(R.id.cartRecyclerView);
        cartCountText = view.findViewById(R.id.cartCountText);
        goToLogInBtn = view.findViewById(R.id.go_to_login);
        goToServicesBtn = view.findViewById(R.id.go_to_service);
        reloadBtn = view.findViewById(R.id.btn_reload_page);

        if (Utility.isOnline(getContext())) {
            reloadPage.setVisibility(View.GONE);
            cartTimeline.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            cartTimeline.setVisibility(View.GONE);
            logInView.setVisibility(View.GONE);
        }

        setUpAdapter();
        onClick();

        return view;
    }

    private void setUpAdapter() {
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        cartsRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        cartsListAdapter = new CartsListAdapter(getActivity());
        cartsRecyclerView.setAdapter(cartsListAdapter);
        cartsListAdapter.notifyDataSetChanged();
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddressActivity.class));
        });
    }
}