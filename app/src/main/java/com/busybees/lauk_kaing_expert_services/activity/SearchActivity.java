package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.busybees.lauk_kaing_expert_services.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.request_object.ProductsCarryObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Search.SearchVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
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

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

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
        CallGetAllHomeApi();
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

        /*if (searchDataModel.getStep() == 1) {

        } else if (searchDataModel.getStep() == 2) {

        } else if (searchDataModel.getStep() == 3) {

        }*/

        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
        productsCarryObject.setPhone(userVO.getPhone());
        productsCarryObject.setServiceId(searchDataModel.getServiceId());
        productsCarryObject.setProductId(searchDataModel.getProductId());
        productsCarryObject.setSubProductId(searchDataModel.getId());
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

        /*ArrayList<ProductsVO> productsVOS = new ArrayList<>();
        productsVOS.clear();

        if (productsVOArrayList.size() != 0) {
            for (int i = 0; i < productsVOArrayList.size(); i++) {

                if (productsVOArrayList.get(i).getServiceId().equals(searchDataModel.getServiceId())) {

                    ProductsVO productsVO = new ProductsVO();
                    productsVO.setServiceId(productsVOArrayList.get(i).getServiceId());
                    productsVO.setProductId(productsVOArrayList.get(i).getProductId());
                    productsVO.setStep(productsVOArrayList.get(i).getStep());
                    productsVO.setName(productsVOArrayList.get(i).getName());
                    productsVO.setNameMm(productsVOArrayList.get(i).getNameMm());
                    productsVO.setNameCh(productsVOArrayList.get(i).getNameCh());
                    productsVO.setProductImage(productsVOArrayList.get(i).getProductImage());
                    productsVO.setSubProducts(productsVOArrayList.get(i).getSubProducts());
                    productsVOS.add(productsVO);
                }
            }

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

            if (productsVOS.size() != 0) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("product_title", productName.getText().toString());
                intent.putExtra("sub_product", subProductsVOArrayList);
                intent.putExtra("product", productsVOS);
                Log.e("pstep2>>>", String.valueOf(productsVOS.size()));
                startActivity(intent);

            } else if (productsVOS.size() == 0) {
                Utility.showToast(getApplicationContext(), "Coming Soon");
            }
        }*/

        ProductsCarryObject productsCarryObject = new ProductsCarryObject();
        productsCarryObject.setPhone(userVO.getPhone());
        productsCarryObject.setServiceId(searchDataModel.getServiceId());
        productsCarryObject.setProductId(searchDataModel.getProductId());
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

    public void IntentProductView(SearchDataModel searchDataModel) {

        if (subProductsVOArrayList.size() != 0) {
            ArrayList<SubProductsVO> subProductsVOS = new ArrayList<>();
            subProductsVOS.clear();

            for (int i = 0; i < subProductsVOArrayList.size(); i++) {
                if (subProductsVOArrayList.get(i).getProductId().equals(searchDataModel.getId())) {

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

            Intent intent = new Intent(this, SubProductActivity.class);
            intent.putExtra("sub_product_title", productName.getText().toString());
            intent.putExtra("sub_product", subProductsVOS);
            startActivity(intent);
        }
    }

    private void CallGetAllHomeApi() {

        progressBar.setVisibility(View.VISIBLE);

        if (Utility.isOnline(getApplicationContext())) {
            networkServiceProvider.GetHomeCall(ApiConstants.BASE_URL + ApiConstants.GET_ALL_HOME).enqueue(new Callback<GetAllHomeModel>() {

                @Override
                public void onResponse(Call<GetAllHomeModel> call, Response<GetAllHomeModel> response) {

                    if (response.body().getError() == false) {
                        progressBar.setVisibility(View.GONE);

                        progressBar.setVisibility(View.GONE);

                        serviceAvailableVOArrayList.addAll(response.body().getData().getServiceAvailable());

                        productsVOArrayList.clear();
                        subProductsVOArrayList.clear();

                        if (response.body().getData().getServiceAvailable() != null) {
                            for (int i = 0; i < response.body().getData().getServiceAvailable().size(); i++) {

                                productsVOArrayList.addAll(response.body().getData().getServiceAvailable().get(i).getProducts());

                                if ((response.body().getData().getServiceAvailable().size() - 1) == i) {
                                    for (int j = 0; j < productsVOArrayList.size(); j++) {
                                        if (productsVOArrayList.get(j).getSubProducts() != null) {
                                            subProductsVOArrayList.addAll(productsVOArrayList.get(j).getSubProducts());
                                        }
                                    }
                                }

                            }
                        }


                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<GetAllHomeModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            progressBar.setVisibility(View.GONE);
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
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
