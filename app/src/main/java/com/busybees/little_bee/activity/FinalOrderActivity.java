package com.busybees.little_bee.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.little_bee.Dialog.DialogServiceDelete;
import com.busybees.little_bee.EventBusModel.EventBusCall;
import com.busybees.little_bee.EventBusModel.EventBusCartObj;
import com.busybees.little_bee.EventBusModel.GoToCart;
import com.busybees.little_bee.MainActivity;
import com.busybees.little_bee.R;
import com.busybees.little_bee.adapters.Orders.FinalOrderAdapter;
import com.busybees.little_bee.data.models.AddToCart.AddToCartModel;
import com.busybees.little_bee.data.models.AddToCart.AddToCartObj;
import com.busybees.little_bee.data.models.GetAllHomeModel;
import com.busybees.little_bee.data.models.GetCart.GetCartDataModel;
import com.busybees.little_bee.data.models.GetCart.GetCartModel;
import com.busybees.little_bee.data.models.GetCart.GetCartObj;
import com.busybees.little_bee.data.models.SaveOrder.SaveOrderObject;
import com.busybees.little_bee.data.vos.Address.AddressVO;
import com.busybees.little_bee.data.vos.Home.ServiceAvailableVO;
import com.busybees.little_bee.data.vos.ServiceDetail.ProductsVO;
import com.busybees.little_bee.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.little_bee.data.vos.Users.UserVO;
import com.busybees.little_bee.network.NetworkServiceProvider;
import com.busybees.little_bee.utility.ApiConstants;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;
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
    private boolean continue_error;
    private String continue_error_msg;

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

                        if (response.body().getError() == false) {
                            reloadPage.setVisibility(View.GONE);

                            if (response.body().getContinueError() != null) {
                                continue_error = response.body().getContinueError();
                                continue_error_msg = response.body().getContinueAlertMsg();
                            }

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
                                    amountText.setText(getString(R.string.currency) + " " + response.body().getTotal());
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
                        } else {
                            Utility.showToast(getApplicationContext(), response.body().getMessage());
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
            if (continue_error == true) {
                LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                final View priceZeroDialogView = factory.inflate(R.layout.dialog_alert_price_zero, null);
                final AlertDialog priceZeroDialog = new AlertDialog.Builder(getApplicationContext()).create();
                priceZeroDialog.setView(priceZeroDialogView);

                priceZeroDialog.setCancelable(false);
                priceZeroDialog.setCanceledOnTouchOutside(false);

                if (priceZeroDialog != null && priceZeroDialog.getWindow() != null) {
                    priceZeroDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    priceZeroDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                }

                TextView alertMsg = priceZeroDialogView.findViewById(R.id.price_zero_alert_txt);

                if (checkLng(getApplicationContext()).equalsIgnoreCase("it") || checkLng(getApplicationContext()).equalsIgnoreCase("fr")) {

                    if (MDetect.INSTANCE.isUnicode()) {
                        alertMsg.setText(getString(R.string.continue_error_msg));
                    } else {
                        alertMsg.setText(getString(R.string.continue_error_msg));
                    }

                } else {
                    alertMsg.setText(continue_error_msg);
                }

                priceZeroDialogView.findViewById(R.id.price_zero_ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        priceZeroDialog.dismiss();
                    }
                });

                priceZeroDialog.show();

            } else {
                Intent intent = new Intent(FinalOrderActivity.this, PaymentActivity.class);
                intent.putExtra("address", addressVO);
                intent.putExtra("date", dateText.getText().toString());
                intent.putExtra("time", timeText.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
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

    public static String checkLng(Context activity) {
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if (lang == null) {
            lang = "en";
        }
        return lang;
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
