package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Search.SearchAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Search.SearchVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private NetworkServiceProvider networkServiceProvider;
    private UserVO userVO;

    private ImageView back, searchImage, searchCancel;
    private TextView productName;

    private EditText searchText;
    private ProgressBar progressBar;
    private RecyclerView searchRecyclerView;
    private LinearLayoutManager layoutManager;
    private SearchAdapter searchAdapter;

    private ArrayList<SearchDataModel> searchList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_search);

        networkServiceProvider = new NetworkServiceProvider(this);
        userVO = Utility.query_UserProfile(this);

        back = findViewById(R.id.back_button);
        searchImage = findViewById(R.id.search_img);
        searchCancel = findViewById(R.id.search_cancel);
        searchText = findViewById(R.id.txt_search);
        progressBar = findViewById(R.id.materialLoader);
        searchRecyclerView = findViewById(R.id.recycle_search);
        productName = findViewById(R.id.s_name);

        onClick();
        setUpAdapter();
    }

    private void setUpAdapter() {
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        searchRecyclerView.setLayoutManager(layoutManager);
    }

    private void onClick() {
        back.setOnClickListener(v -> {
          finish();
        });

        searchImage.setOnClickListener(v -> {
            String searchName = searchText.getText().toString();
            if (!searchName.isEmpty()){
                SearchApi(searchName);
            }
        });

        searchCancel.setOnClickListener(v -> {
            searchCancel.setVisibility(View.GONE);
            searchImage.setVisibility(View.VISIBLE);
            searchText.setText("");
        });

        searchText.setText("");
        searchText.requestFocus();

        searchText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:

                        progressBar.setVisibility(View.VISIBLE);

                        String searchName = searchText.getText().toString();
                        if (!searchName.isEmpty()){
                            SearchApi(searchName);
                        }

                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
    }

    private void SearchApi(String searchName) {
        SearchVO searchObj = new SearchVO();
        searchObj.setSearch(searchName);
        CallSearch(searchObj);
    }

    private void CallSearch(SearchVO searchVO) {

        if (Utility.isOnline(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);

            networkServiceProvider.Search(ApiConstants.BASE_URL + ApiConstants.GET_SEARCH_PRODUCTS, searchVO).enqueue(new Callback<SearchModel>() {
                @Override
                public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                    if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);

                        Utility.hideKeyboard(SearchActivity.this);
                        searchList.clear();
                        searchList.addAll(response.body().getData());
                        searchAdapter = new SearchAdapter(SearchActivity.this, searchList);
                        searchRecyclerView.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();
                        searchImage.setVisibility(View.GONE);
                        searchCancel.setVisibility(View.VISIBLE);

                        searchAdapter.setClick(SearchActivity.this);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SearchModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getMessage());

                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    public void IntentServiceDetailView(SearchDataModel searchDataModel) {

        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
        productsCarryObject.setPhone(userVO.getPhone());
        productsCarryObject.setServiceId(searchDataModel.getServiceId());
        productsCarryObject.setStep(searchDataModel.getStep());

        if (Utility.checkLng(getApplicationContext()).equalsIgnoreCase("it") || Utility.checkLng(getApplicationContext()).equalsIgnoreCase("fr")){
            if ( MDetect.INSTANCE.isUnicode()){

                Utility.addFontSuHome(productName, searchDataModel.getNameMm());

            } else  {

                Utility.changeFontUni2ZgHome(productName, searchDataModel.getNameMm());
            }
        } else if (Utility.checkLng(getApplicationContext()).equalsIgnoreCase("zh")) {
            productName.setText(searchDataModel.getNameCh());
        } else {
            productName.setText(searchDataModel.getName());
        }

        Intent intent = new Intent(getApplicationContext(), ServiceDetailActivity.class);
        intent.putExtra("product_title", productName.getText().toString());
        intent.putExtra("product_detail_data", productsCarryObject);
        startActivity(intent);

    }

    public void IntentSubProductView(SearchDataModel searchDataModel) {

        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
        productsCarryObject.setPhone(userVO.getPhone());
        productsCarryObject.setProductPriceId(searchDataModel.getProductPriceId());
        productsCarryObject.setSubProductId(searchDataModel.getSubProductId());
        productsCarryObject.setProductId(searchDataModel.getProductId());
        productsCarryObject.setServiceId(searchDataModel.getServiceId());
        productsCarryObject.setStep(searchDataModel.getStep());

        if (Utility.checkLng(getApplicationContext()).equalsIgnoreCase("it") || Utility.checkLng(getApplicationContext()).equalsIgnoreCase("fr")){
            if ( MDetect.INSTANCE.isUnicode()){

                Utility.addFontSuHome(productName, searchDataModel.getNameMm());

            } else  {

                Utility.changeFontUni2ZgHome(productName, searchDataModel.getNameMm());
            }
        } else if (Utility.checkLng(getApplicationContext()).equalsIgnoreCase("zh")) {
            productName.setText(searchDataModel.getNameCh());
        } else {
            productName.setText(searchDataModel.getName());
        }

        Intent intent = new Intent(getApplicationContext(), ServiceDetailActivity.class);
        intent.putExtra("product_title", productName.getText().toString());
        intent.putExtra("product_detail_data", productsCarryObject);
        startActivity(intent);

    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
