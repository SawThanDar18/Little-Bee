package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.FinalOrderAdapter;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class FinalOrderActivity extends AppCompatActivity {

    private RecyclerView finalOrderRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private FinalOrderAdapter finalOrderAdapter;

    private RelativeLayout logInView, addMoreServiceLayout;
    private LinearLayout reloadPage, continueLayout;

    private ImageView back, homePageView, cartPageView, addressPageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_final_order);

        logInView = findViewById(R.id.loginView);
        reloadPage = findViewById(R.id.reload_page);
        continueLayout = findViewById(R.id.continue_layout);
        addMoreServiceLayout = findViewById(R.id.btn_add_more_service);
        finalOrderRecyclerView = findViewById(R.id.final_order_recyclerview);
        back = findViewById(R.id.back_button);
        homePageView = findViewById(R.id.home_page_btn);
        cartPageView = findViewById(R.id.cart_page_btn);
        addressPageView = findViewById(R.id.address_page_btn);

        if (Utility.isOnline(getApplicationContext())) {
            reloadPage.setVisibility(View.GONE);
            logInView.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.GONE);
        }

        setUpAdapter();
        onClick();
    }

    private void setUpAdapter() {
        recyclerViewLayoutManager = new LinearLayoutManager(FinalOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        finalOrderRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        finalOrderAdapter = new FinalOrderAdapter(FinalOrderActivity.this);
        finalOrderRecyclerView.setAdapter(finalOrderAdapter);
        finalOrderAdapter.notifyDataSetChanged();
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
        });

        addMoreServiceLayout.setOnClickListener(v -> {
            AddMoreServicesDialog addMoreServicesDialog = new AddMoreServicesDialog();
            addMoreServicesDialog.show(getSupportFragmentManager(), "");
        });

        back.setOnClickListener(v -> finish());

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(FinalOrderActivity.this, MainActivity.class));
            finish();
        });

        cartPageView.setOnClickListener(v -> {

        });

        addressPageView.setOnClickListener(v -> finish());
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
