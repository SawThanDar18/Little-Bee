package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ProductAdapter;

public class ProductActivity extends AppCompatActivity {

    private TextView productName;
    private RecyclerView productRecyclerView;

    private ProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.product_name);
        productRecyclerView = findViewById(R.id.product_recyclerview);

        setUpAdapter();

    }

    private void setUpAdapter() {
        productAdapter= new ProductAdapter(this);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

        productAdapter.setClick(this);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    public void intentToSubProductActivity () {
        startActivity(new Intent(getApplicationContext(), SubProductActivity.class));
    }
}
