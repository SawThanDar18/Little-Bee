package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.lauk_kaing_expert_services.Dialog.DialogServiceDelete;
import com.busybees.lauk_kaing_expert_services.EventBusModel.EventBusCall;
import com.busybees.lauk_kaing_expert_services.EventBusModel.EventBusCartObj;
import com.busybees.lauk_kaing_expert_services.EventBusModel.GoToCart;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Carts.CartsListAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.AvailableAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.PopularAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Home.SymnAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.FinalOrderAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.AddToCart.AddToCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.GetAllHomeModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.SaveOrderObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.AddressVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalOrderActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;
    private UserVO userVO;

    private Bundle bundle;

    private RecyclerView finalOrderRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private FinalOrderAdapter finalOrderAdapter;

    private RelativeLayout logInView, addMoreServiceLayout, noServiceView;
    private ConstraintLayout finalOrderTimeLine;
    private LinearLayout reloadPage, continueLayout;
    private Button goToServicesBtn;

    private TextView addressText, dateText, timeText, cartCountText, amountText;
    private ImageView back, homePageView, cartPageView, addressPageView;

    private ProgressBar progressBar;

    private ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<>();
    private ArrayList<ProductsVO> productsVOArrayList = new ArrayList<>();
    private ArrayList<SubProductsVO> subProductsVOArrayList = new ArrayList<>();

    private AddressVO addressVO;

    ArrayList<GetCartDataModel> cartDatas = new ArrayList<>();

    JSONObject jsonObject;

    private SaveOrderObject saveOrderObject = new SaveOrderObject();

    private boolean isCartItemAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_final_order);

        serviceProvider = new NetworkServiceProvider(getApplicationContext());
        userVO = Utility.query_UserProfile(this);

        finalOrderTimeLine = findViewById(R.id.final_order_timeline);
        noServiceView = findViewById(R.id.no_service_view);
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
        addressText = findViewById(R.id.addressText);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        cartCountText = findViewById(R.id.cartCountText);
        amountText = findViewById(R.id.amountText);
        goToServicesBtn = findViewById(R.id.go_to_service);

        if (Utility.isOnline(getApplicationContext())) {
            reloadPage.setVisibility(View.GONE);
            logInView.setVisibility(View.VISIBLE);
        } else {
            reloadPage.setVisibility(View.VISIBLE);
            logInView.setVisibility(View.GONE);
        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            addressVO = (AddressVO) getIntent().getSerializableExtra("address");
        }

        try {
            jsonObject = new JSONObject(bundle.getString(AppENUM.KeyName.PRODUCT_DETAIL, ""));
            timeText.setText(jsonObject.getString(AppENUM.KeyName.TIME));
            dateText.setText(jsonObject.getString(AppENUM.KeyName.DATE));
            addressText.setText(jsonObject.getString("full_address"));

        } catch (Exception e) {
            Log.d("error " + getClass().getName(), e.getMessage());
        }

        CallGetAllHomeApi();
        setUpAdapter();
        onClick();
        CallGetCart();
    }

    private void setUpAdapter() {
        recyclerViewLayoutManager = new LinearLayoutManager(FinalOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        finalOrderRecyclerView.setLayoutManager(recyclerViewLayoutManager);
    }

    private void CallGetCart() {
        progressBar.setVisibility(View.VISIBLE);
        if (userVO != null) {
            GetCartObj cartObj = new GetCartObj();
            cartObj.setPhone(userVO.getPhone());

            if (Utility.isOnline(getApplicationContext())) {

                serviceProvider.GetCartCall(ApiConstants.BASE_URL + ApiConstants.GET_CART, cartObj).enqueue(new Callback<GetCartModel>() {
                    @Override
                    public void onResponse(Call<GetCartModel> call, Response<GetCartModel> response) {

                        reloadPage.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        cartDatas.clear();
                        cartDatas.addAll(response.body().getData());
                        finalOrderAdapter = new FinalOrderAdapter(FinalOrderActivity.this, cartDatas);
                        finalOrderRecyclerView.setAdapter(finalOrderAdapter);
                        finalOrderAdapter.notifyDataSetChanged();

                        finalOrderAdapter.setCLick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AdapterClick(v);
                            }
                        });

                        if (!cartDatas.isEmpty() && cartDatas != null) {
                            continueLayout.setVisibility(View.VISIBLE);
                            isCartItemAvailable = true;
                            finalOrderTimeLine.setVisibility(View.VISIBLE);
                            noServiceView.setVisibility(View.GONE);

                            cartCountText.setText(String.valueOf(response.body().getTotalQuantity()));
                            if (response.body().getTotal() != 0) {
                                amountText.setText(response.body().getTotal() + " " + getString(R.string.currency));
                            } else {
                                amountText.setText("Survey");
                            }

                        } else {
                            continueLayout.setVisibility(View.GONE);
                            isCartItemAvailable = false;
                            finalOrderTimeLine.setVisibility(View.GONE);
                            noServiceView.setVisibility(View.GONE);

                            cartCountText.setText("0");
                            amountText.setText(" ");
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCartModel> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), t.getMessage());
                    }
                });

            } else {
                progressBar.setVisibility(View.GONE);
                Utility.showToast(getApplicationContext(), getString(R.string.no_internet));

            }
        }
    }

    private void AdapterClick(View v) {

        if (v.getId() == R.id.decreseBtn) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            int quantity = dataModel.getQuantity();

            if (quantity == 1) {
                Utility.showToast(getApplicationContext(), getString(R.string.qty_zero));
            } else {

                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        if (cartDatas != null) {
                            if (cartDatas.isEmpty()) {
                                Utility.showToast(getApplicationContext(), getString(R.string.no_item_cart));
                            }
                        }
                        dataModel.setQuantity(Integer.valueOf(quantity + ""));

                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(dataModel.getQuantity());
                        addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                        addToCartObj.setFormStatus(dataModel.getFormStatus());

                        CallAddToCart(addToCartObj);
                    } else {
                        dataModel.setQuantity(Integer.valueOf(quantity + ""));

                        AddToCartObj addToCartObj = new AddToCartObj();
                        addToCartObj.setPhone(userVO.getPhone());
                        addToCartObj.setQuantity(dataModel.getQuantity());
                        addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                        addToCartObj.setFormStatus(dataModel.getFormStatus());

                        CallAddToCart(addToCartObj);
                    }
                }

            }

        } else if (v.getId() == R.id.increaseBtn) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            int quantity = dataModel.getQuantity();
            if (quantity < 10) {
                dataModel.setQuantity(Integer.valueOf((quantity + 1) + ""));

                AddToCartObj addToCartObj = new AddToCartObj();
                addToCartObj.setPhone(userVO.getPhone());
                addToCartObj.setQuantity(dataModel.getQuantity());
                addToCartObj.setProductPriceId(dataModel.getProductPriceId());
                addToCartObj.setFormStatus(dataModel.getFormStatus());

                CallAddToCart(addToCartObj);

            } else {
                Utility.showToast(getApplicationContext(), getString(R.string.cant_update_more_ten));
            }
        } else if (v.getId() == R.id.delete_cart) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel dataModel = cartDatas.get(position);
            dataModel.setQuantity(0);
            DialogServiceDelete dialogServiceDelete = new DialogServiceDelete(dataModel);
            dialogServiceDelete.show(getSupportFragmentManager(), "");

        } else if (v.getId() == R.id.cancel) {
            int position = (int) v.getTag(R.id.position);
            GetCartDataModel sdpModel = cartDatas.get(position);
            int quantity = Integer.parseInt(String.valueOf(sdpModel.getQuantity()));
            if(Double.parseDouble(String.valueOf(cartDatas.get(position).getFormStatus())) == 2) {
                AddToCartObj addToCartObj = new AddToCartObj();
                addToCartObj.setPhone(userVO.getPhone());
                addToCartObj.setProductPriceId(cartDatas.get(position).getProductPriceId());
                addToCartObj.setQuantity(0);
                addToCartObj.setFormStatus(2);
                CallAddToCart(addToCartObj);

            }else {
                if (quantity > 0) {
                    quantity--;
                    if (quantity == 0) {
                        if (cartDatas != null) {
                            if (cartDatas.isEmpty()) {
                                Utility.showToast(getApplicationContext(), getString(R.string.no_item_cart));
                            }
                        }
                        sdpModel.setQuantity(0);
                        DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                        serviceDelete.show(getSupportFragmentManager(), "");
                    } else {
                        sdpModel.setQuantity(0);
                        DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                        serviceDelete.show(getSupportFragmentManager(), "");

                    }
                } else {
                    sdpModel.setQuantity(0);
                    DialogServiceDelete serviceDelete = new DialogServiceDelete(sdpModel);
                    serviceDelete.show(getSupportFragmentManager(), "");
                }
            }


        }
    }

    public void CallAddToCart(AddToCartObj obj) {

        if (Utility.isOnline(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);

            serviceProvider.AddToCartCall(ApiConstants.BASE_URL + ApiConstants.GET_ADD_TO_CART, obj).enqueue(new Callback<AddToCartModel>() {
                @Override
                public void onResponse(Call<AddToCartModel> call, Response<AddToCartModel> response) {

                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError() == true) {

                        Utility.showToast(getApplicationContext(), response.body().getMessage());

                    } else if (response.body().getError() == false) {

                        CallGetCart();
                        //EventBus.getDefault().post("refreshdata");

                    }

                }

                @Override
                public void onFailure(Call<AddToCartModel> call, Throwable t) {
                    Utility.showToast(getApplicationContext(), t.getMessage());
                }
            });

        } else {
            Utility.showToast(getApplicationContext(), getString(R.string.no_internet));
        }
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
            Intent intent = new Intent(FinalOrderActivity.this, PaymentActivity.class);
            intent.putExtra("address", addressVO);
            intent.putExtra("date", dateText.getText().toString());
            intent.putExtra("time", timeText.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
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

        goToServicesBtn.setOnClickListener(v -> {
            EventBusCall service = new EventBusCall();
            service.setService("1");
            EventBus.getDefault().post(service);
        });
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getServiceDeleteID(EventBusCartObj obj) {
        AddToCartObj addToCartObj = new AddToCartObj();
        addToCartObj.setQuantity(obj.getQuantity());
        addToCartObj.setPhone(obj.getPhone());
        addToCartObj.setProductPriceId(obj.getId());
        addToCartObj.setFormStatus(obj.getFormStatus());

        CallAddToCart(addToCartObj);

    }

    @Subscribe
    public void getEventBusFinalRefresh(String refreshdata) {

        CallGetCart();

    }

    @Subscribe
    public void getEventBusCart(String home) {

        userVO = Utility.query_UserProfile(getApplicationContext());
    }
}
