package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.SubProductAdapter;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;

public class SubProductActivity extends AppCompatActivity {

    private TextView subProductName;
    private ImageView back;

    private RecyclerView subProductRecyclerView;

    private SubProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_sub_product);

        subProductName = findViewById(R.id.sub_product_name);
        back = findViewById(R.id.back_button);
        subProductRecyclerView = findViewById(R.id.sub_product_recyclerview);

        setUpAdapter();
        onRecyclerViewClick();
        onClick();

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());
    }

    private void onRecyclerViewClick() {
        subProductRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, subProductRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                            startActivity(new Intent(SubProductActivity.this, ServiceDetailActivity.class));

                    }

                    @Override public void onLongItemClick(View view, int position) {

                               // startActivity(new Intent(SubProductActivity.this, ImageViewActivity.class));

                    }
                })
        );

    }

    private void setUpAdapter() {
        productAdapter= new SubProductAdapter(this);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        subProductRecyclerView.setLayoutManager(layoutManager);
        subProductRecyclerView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

        productAdapter.setClick(this);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
