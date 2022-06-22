package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Payment.PaymentAdapter;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView paymentRecyclerView;
    private PaymentAdapter adapter;
    private LinearLayoutManager paymentLayoutManager;

    private LinearLayout continueLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_payment);

        paymentRecyclerView = findViewById(R.id.recycle_payment);
        continueLayout = findViewById(R.id.continue_layout);

        setUpAdapter();
        onClick();
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            startActivity(new Intent(PaymentActivity.this, ThanksActivity.class));
        });
    }

    private void setUpAdapter() {
        adapter = new PaymentAdapter(PaymentActivity.this);
        paymentLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        paymentRecyclerView.setLayoutManager(paymentLayoutManager);
        paymentRecyclerView.setAdapter(adapter);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
