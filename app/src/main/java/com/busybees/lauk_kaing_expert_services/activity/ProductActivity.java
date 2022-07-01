package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Context;
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

import com.busybees.data.vos.ServiceDetail.ProductsVO;
import com.busybees.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Products.ProductAdapter;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private TextView productName;
    private ImageView back;

    private RecyclerView productRecyclerView;

    private ProductAdapter productAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // Intent Data
    private String productTitle;
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_product);

        productName = findViewById(R.id.product_name);
        back = findViewById(R.id.back_button);
        productRecyclerView = findViewById(R.id.product_recyclerview);

        setUpAdapter();
        onClick();

        if (getIntent() != null) {
            productTitle = getIntent().getStringExtra("product_title");
            productsVOArrayList = (ArrayList<ProductsVO>) getIntent().getSerializableExtra("product");
            subProductsVOArrayList = (ArrayList<SubProductsVO>) getIntent().getSerializableExtra("sub_product");

            productName.setText(productTitle);

            if (productsVOArrayList.size() != 0){

                productAdapter= new ProductAdapter(this, productsVOArrayList);
                productRecyclerView.setAdapter(productAdapter);
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

    private void setUpAdapter() {
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(layoutManager);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    public void intentToSubProductActivity (ProductsVO productsVO) {
        if (subProductsVOArrayList.size() != 0){
            ArrayList<SubProductsVO> subProductsVOS = new ArrayList<>();
            subProductsVOS.clear();

            for (int i = 0; i < subProductsVOArrayList.size(); i++) {
                if (subProductsVOArrayList.get(i).getProductId().equals(productsVO.getProductId())) {

                    SubProductsVO subProductsVO = new SubProductsVO();
                    subProductsVO.setServiceId(subProductsVOArrayList.get(i).getServiceId());
                    subProductsVO.setProductId(subProductsVOArrayList.get(i).getProductId());
                    subProductsVO.setSubProductId(subProductsVOArrayList.get(i).getSubProductId());
                    subProductsVO.setStep(subProductsVOArrayList.get(i).getStep());
                    subProductsVO.setName(subProductsVOArrayList.get(i).getName());
                    subProductsVO.setNameMm(subProductsVOArrayList.get(i).getNameMm());
                    subProductsVO.setNameCh(subProductsVOArrayList.get(i).getNameCh());
                    subProductsVO.setSubProductImage(subProductsVOArrayList.get(i).getSubProductImage());
                    subProductsVO.setSubProductVideo(subProductsVOArrayList.get(i).getSubProductVideo());
                    subProductsVOS.add(subProductsVO);
                }
            }

            if (checkLng(getApplicationContext()).equalsIgnoreCase("it")){
                Utility.addFontSuHome(productName, productsVO.getNameMm());
            } else if (checkLng(getApplicationContext()).equalsIgnoreCase("fr")) {
                Utility.changeFontZg2UniHome(productName, productsVO.getNameMm());
            } else if (checkLng(getApplicationContext()).equalsIgnoreCase("zh")) {
                productName.setText(productsVO.getNameCh());
            } else {
                productName.setText(productsVO.getName());
            }

            Intent intent = new Intent(this, SubProductActivity.class);
            intent.putExtra("sub_product_title", productName.getText().toString());
            intent.putExtra("sub_product", subProductsVOS);
            startActivity(intent);
        }
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}
