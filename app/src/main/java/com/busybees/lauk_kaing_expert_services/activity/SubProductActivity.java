package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.SubProductAdapter;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.RecyclerItemClickListener;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;

public class SubProductActivity extends AppCompatActivity {

    private TextView subProductName, subProduct_Name;
    private ImageView back;

    private RecyclerView subProductRecyclerView;

    private SubProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Intent Data
    private String subProductTitle;
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //makeStatusBarVisible();
        makeFullScreen();
        setContentView(R.layout.activity_sub_product);

        subProductName = findViewById(R.id.sub_product_name);
        subProduct_Name = findViewById(R.id.s_name);
        back = findViewById(R.id.back_button);
        subProductRecyclerView = findViewById(R.id.sub_product_recyclerview);

        setUpAdapter();
        onRecyclerViewClick();
        onClick();

        if (getIntent() != null) {
            subProductTitle = getIntent().getStringExtra("sub_product_title");
            subProductsVOArrayList = (ArrayList<SubProductsVO>) getIntent().getSerializableExtra("sub_product");

            subProductName.setText(subProductTitle);

            if (subProductsVOArrayList.size() != 0){

                productAdapter= new SubProductAdapter(this, subProductsVOArrayList);
                subProductRecyclerView.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();

                productAdapter.setClick(this);

            }else {
                finish();
            }
        }

    }

    private void onClick() {
        back.setOnClickListener(v -> finish());
    }

    private void onRecyclerViewClick() {
        subProductRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, subProductRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
                        productsCarryObject.setServiceId(subProductsVOArrayList.get(position).getServiceId());
                        productsCarryObject.setProductId(subProductsVOArrayList.get(position).getProductId());
                        productsCarryObject.setSubProductId(subProductsVOArrayList.get(position).getSubProductId());
                        productsCarryObject.setStep(subProductsVOArrayList.get(position).getStep());

                        if (checkLng(getApplicationContext()).equalsIgnoreCase("it") || checkLng(getApplicationContext()).equalsIgnoreCase("fr")){
                            if ( MDetect.INSTANCE.isUnicode()){

                                Utility.addFontSuHome(subProduct_Name, subProductsVOArrayList.get(position).getNameMm());

                            } else  {

                                Utility.changeFontUni2ZgHome(subProduct_Name, subProductsVOArrayList.get(position).getNameMm());
                            }
                        } else if (checkLng(getApplicationContext()).equalsIgnoreCase("zh")) {
                            subProduct_Name.setText(subProductsVOArrayList.get(position).getNameCh());
                        } else {
                            subProduct_Name.setText(subProductsVOArrayList.get(position).getName());
                        }

                        Intent intent = new Intent(getApplicationContext(), ServiceDetailActivity.class);
                        intent.putExtra("product_title", subProduct_Name.getText().toString());
                        intent.putExtra("product_detail_data", productsCarryObject);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {

                               // startActivity(new Intent(SubProductActivity.this, ImageViewActivity.class));

                    }
                })
        );

    }

    private void setUpAdapter() {
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        subProductRecyclerView.setLayoutManager(layoutManager);
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
