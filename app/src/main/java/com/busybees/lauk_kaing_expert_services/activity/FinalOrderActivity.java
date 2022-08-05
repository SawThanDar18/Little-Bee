package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.lauk_kaing_expert_services.EventBusModel.GoToCart;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Home.AvailableAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.PopularAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.SymnAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.FinalOrderAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalOrderActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;

    private RecyclerView finalOrderRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private FinalOrderAdapter finalOrderAdapter;

    private RelativeLayout logInView, addMoreServiceLayout;
    private LinearLayout reloadPage, continueLayout;

    private ImageView back, homePageView, cartPageView, addressPageView;

    private ProgressBar progressBar;

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_final_order);

        serviceProvider = new NetworkServiceProvider(getApplicationContext());

        logInView = findViewById(R.id.loginView);
        reloadPage = findViewById(R.id.reload_page);
        continueLayout = findViewById(R.id.continue_layout);
        addMoreServiceLayout = findViewById(R.id.btn_add_more_service);
        finalOrderRecyclerView = findViewById(R.id.final_order_recyclerview);
        back = findViewById(R.id.back_button);
        homePageView = findViewById(R.id.home_page_btn);
        cartPageView = findViewById(R.id.cart_page_btn);
        addressPageView = findViewById(R.id.address_page_btn);
        progressBar = findViewById(R.id.materialLoader);

        if (Utility.isOnline(getApplicationContext())) {
            reloadPage.setVisibility(View.GONE);
            logInView.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.GONE);
        }

        CallGetAllHomeApi();
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

    private void CallGetAllHomeApi() {

        if (Utility.isOnline(getApplicationContext())) {
            serviceProvider.GetHomeCall(ApiConstants.BASE_URL + ApiConstants.GET_ALL_HOME).enqueue(new Callback<GetAllHomeModel>() {

                @Override
                public void onResponse(Call<GetAllHomeModel> call, Response<GetAllHomeModel> response) {

                    progressBar.setVisibility(View.GONE);
                    reloadPage.setVisibility(View.GONE);

                    serviceAvailableVOArrayList.clear();
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

                }

                @Override
                public void onFailure(Call<GetAllHomeModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
        });

        addMoreServiceLayout.setOnClickListener(v -> {
            if (serviceAvailableVOArrayList != null && productsVOArrayList != null && subProductsVOArrayList != null) {
                AddMoreServicesDialog addMoreServicesDialog = new AddMoreServicesDialog(serviceAvailableVOArrayList, productsVOArrayList, subProductsVOArrayList);
                addMoreServicesDialog.show(getSupportFragmentManager(), "");
            }

        });

        back.setOnClickListener(v -> finish());

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(FinalOrderActivity.this, MainActivity.class));
            finish();
        });

        cartPageView.setOnClickListener(v -> {
            GoToCart goToCart = new GoToCart();
            goToCart.setText("go");
            EventBus.getDefault().postSticky(goToCart);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        addressPageView.setOnClickListener(v -> finish());
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
