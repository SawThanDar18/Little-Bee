package com.busybees.lauk_kaing_expert_services.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.EventBusModel.GoToCart;
import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Payment.PaymentAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartDataModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartModel;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartObj;
import com.busybees.lauk_kaing_expert_services.data.models.MatchPromoCodeModel;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.ProductDetailObject;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.SaveOrderModel;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrder.SaveOrderObject;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.AddressVO;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.MatchPromoCodeObject;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.ProductPriceListVO;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.PromoCodeVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PaymentActivity extends AppCompatActivity {

    private NetworkServiceProvider serviceProvider;
    private UserVO userObj = new UserVO();

    private RecyclerView paymentRecyclerView;
    private PaymentAdapter adapter;
    private LinearLayoutManager paymentLayoutManager;

    private LinearLayout continueLayout, viewSubTotal, viewDiscount, viewTotal, viewPromoCode;

    private ProgressBar progressBar;
    private TextView subtotal, discountTotal, total, promo_discount, applyPromoCode;

    private ImageView back, homePageView, cartPageView, addressPageView, finalOrderPageView;

    ArrayList<GetCartDataModel> cartDatas = new ArrayList<>();
    ArrayList<PromoCodeVO> promoCodeVOArrayList = new ArrayList<>();

    private ArrayList<ProductPriceListVO> productPriceListVOArrayList = new ArrayList<>();
    private MatchPromoCodeObject matchPromoCodeObject = new MatchPromoCodeObject();

    private ArrayList<ProductDetailObject> productDetailObjectArrayList = new ArrayList<>();
    private SaveOrderObject saveOrderObject = new SaveOrderObject();

    private ArrayList<String> list;
    private int discount, promoId;
    private String promoCode;

    private AddressVO addressVO = new AddressVO();
    private String orderDate, orderTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_payment);

        serviceProvider = new NetworkServiceProvider(this);
        userObj = Utility.query_UserProfile(this);

        paymentRecyclerView = findViewById(R.id.recycle_payment);
        continueLayout = findViewById(R.id.continue_layout);
        back = findViewById(R.id.back_button);
        homePageView = findViewById(R.id.home_page_btn);
        cartPageView = findViewById(R.id.cart_page_btn);
        addressPageView = findViewById(R.id.address_page_btn);
        finalOrderPageView = findViewById(R.id.final_order_page_btn);
        progressBar = findViewById(R.id.materialLoader);
        subtotal = findViewById(R.id.sub_total_amount);
        discountTotal = findViewById(R.id.discount_total_amount);
        total = findViewById(R.id.total_amount);
        viewSubTotal = findViewById(R.id.sub_total_view);
        viewDiscount = findViewById(R.id.discount_view);
        viewTotal = findViewById(R.id.total_view);
        viewPromoCode = findViewById(R.id.promo_discount_view);
        promo_discount = findViewById(R.id.promo_amount);
        applyPromoCode = findViewById(R.id.apply_promo_code);

        if (getIntent() != null) {
            addressVO = (AddressVO) getIntent().getSerializableExtra("address");
            orderDate = getIntent().getStringExtra("date");
            orderTime = getIntent().getStringExtra("time");
        }

        setUpAdapter();
        onClick();
        CallGetCart();

    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
            saveOrderObject.setDate(orderDate);
            saveOrderObject.setTime(orderTime);
            saveOrderObject.setOrderSource("android");
            saveOrderObject.setPhone(userObj.getPhone());
            saveOrderObject.setPaymentType("cash");
            saveOrderObject.setAddressId(addressVO.getId());
            SaveOrder(saveOrderObject);
        });

        applyPromoCode.setOnClickListener( v-> {
            showPromoCodeDialog();
            /*if (promoCode == null) {
                showPromoCodeDialog();
            } else {
                if (list.size() == 1) {
                    //save order
                } else {
                    showConfirmDialog();
                }
            }*/
        });

        back.setOnClickListener(v -> {
            finish();
        });


        cartPageView.setOnClickListener(v -> {
            GoToCart goToCart = new GoToCart();
            goToCart.setText("go");
            EventBus.getDefault().postSticky(goToCart);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

        homePageView.setOnClickListener(v -> {
            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            finish();
        });

        addressPageView.setOnClickListener(v -> {
            startActivity(new Intent(PaymentActivity.this, AddressActivity.class));
            finish();
        });

        finalOrderPageView.setOnClickListener(v -> finish());

    }

    private void showConfirmDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View promoCodeConfirmDialogView = factory.inflate(R.layout.dialog_confirm, null);
        final AlertDialog promoCodeConfirmDialog = new AlertDialog.Builder(this).create();
        promoCodeConfirmDialog.setView(promoCodeConfirmDialogView);

        promoCodeConfirmDialog.setCancelable(false);
        promoCodeConfirmDialog.setCanceledOnTouchOutside(false);

        if (promoCodeConfirmDialog != null && promoCodeConfirmDialog.getWindow() != null) {
            promoCodeConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            promoCodeConfirmDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        promoCodeConfirmDialogView.findViewById(R.id.promo_confirm_yes_btn).setOnClickListener(v1 -> {

            promoCodeConfirmDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), ThanksActivity.class));

        });

        promoCodeConfirmDialogView.findViewById(R.id.promo_confirm_no_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeConfirmDialog.dismiss();
                showPromoCodeDialog();
            }
        });

        promoCodeConfirmDialog.show();
    }

    private void showPromoCodeDialog() {
        if (promoCodeVOArrayList.isEmpty()) {
            startActivity(new Intent(PaymentActivity.this, ThanksActivity.class));
        } else {
            LayoutInflater factory = LayoutInflater.from(this);
            final View promoCodeDialogView = factory.inflate(R.layout.dialog_choose_promo_code, null);
            final AlertDialog promoCodeDialog = new AlertDialog.Builder(this).create();
            promoCodeDialog.setView(promoCodeDialogView);

            promoCodeDialog.setCancelable(false);
            promoCodeDialog.setCanceledOnTouchOutside(false);

            if (promoCodeDialog != null && promoCodeDialog.getWindow() != null) {
                promoCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                promoCodeDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }

            Spinner services_spinner = promoCodeDialogView.findViewById(R.id.services_spinner);

            TextView promoCodeName = promoCodeDialogView.findViewById(R.id.promo_code_name);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentActivity.this, android.R.layout.simple_spinner_item, list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            services_spinner.setAdapter(arrayAdapter);

            services_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    promoCodeName.setText(promoCodeVOArrayList.get(position).getPromoCode());
                    promoId = promoCodeVOArrayList.get(position).getPromoId();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            promoCodeDialogView.findViewById(R.id.promo_code_ok_btn).setOnClickListener(v1 -> {

                promoCode = promoCodeName.getText().toString();
                matchPromoCodeObject.setPromoCode(promoCode);
                CallMatchPromoCode(matchPromoCodeObject);
                saveOrderObject.setPromoId(promoId);

                promoCodeDialog.dismiss();

            });

            promoCodeDialogView.findViewById(R.id.promo_cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promoCodeDialog.dismiss();
                }
            });

            promoCodeDialog.show();

        }
    }

    private void CallMatchPromoCode(MatchPromoCodeObject matchPromoCodeObject) {
        progressBar.setVisibility(View.VISIBLE);

        if (Utility.isOnline(getApplicationContext())) {
            serviceProvider.MatchPromoCode(ApiConstants.BASE_URL + ApiConstants.GET_MATCH_PROMO_CODE, matchPromoCodeObject).enqueue(new Callback<MatchPromoCodeModel>() {
                @Override
                public void onResponse(Call<MatchPromoCodeModel> call, Response<MatchPromoCodeModel> response) {
                    if (response.body().isError() == false) {
                        progressBar.setVisibility(View.GONE);
                        viewPromoCode.setVisibility(View.VISIBLE);
                        promo_discount.setText("( " + response.body().getData().getDiscount() + " ) " + getString(R.string.currency));
                        int totalFromPromo = response.body().getData().getTotal();
                        int totalAmount = totalFromPromo - discount;
                        if (totalFromPromo == 0) {
                            viewTotal.setVisibility(View.GONE);
                        } else {
                            viewTotal.setVisibility(View.VISIBLE);
                            total.setText(totalFromPromo + " " + getString(R.string.currency));
                        }
                    } else if (response.body().isError() == true) {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(PaymentActivity.this, response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<MatchPromoCodeModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Utility.showToast(getApplicationContext(), t.getLocalizedMessage());
                }
            });
        }
    }

    private void CallGetCart() {
        if (userObj != null) {
            GetCartObj cartObj = new GetCartObj();
            cartObj.setPhone(userObj.getPhone());

            progressBar.setVisibility(View.VISIBLE);

            if (Utility.isOnline(getApplicationContext())) {

                serviceProvider.GetCartCall(ApiConstants.BASE_URL + ApiConstants.GET_CART, cartObj).enqueue(new Callback<GetCartModel>() {
                    @Override
                    public void onResponse(Call<GetCartModel> call, Response<GetCartModel> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.body().getError() == false) {
                            cartDatas.clear();
                            cartDatas.addAll(response.body().getData());
                            SetMatchPromo(cartDatas);

                            if (!cartDatas.isEmpty() && cartDatas != null) {
                                for (int i = 0; i < cartDatas.size(); i++) {

                                    ProductDetailObject productDetailObject = new ProductDetailObject();
                                    productDetailObject.setPriceId(cartDatas.get(i).getProductPriceId());
                                    productDetailObject.setQuantity(cartDatas.get(i).getQuantity());
                                    productDetailObjectArrayList.add(productDetailObject);

                                }

                                saveOrderObject.setProductDetail(productDetailObjectArrayList);
                            }

                            if (response.body().getPromoCodeList() != null) {

                                promoCodeVOArrayList.clear();
                                promoCodeVOArrayList.addAll(response.body().getPromoCodeList());

                                String lang = AppStorePreferences.getString(getApplicationContext(), AppENUM.LANG);
                                list = new ArrayList<String>();

                                if (lang != null) {
                                    if (lang.equalsIgnoreCase("it") || lang.equalsIgnoreCase("fr")) {
                                        if (MDetect.INSTANCE.isUnicode()) {
                                            for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                                if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                                                    list.add(promoCodeVOArrayList.get(i).getServiceNameMm());
                                                } else {
                                                }
                                            }
                                        } else {
                                            for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                                if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                                                    list.add(promoCodeVOArrayList.get(i).getServiceNameMm());
                                                } else {
                                                }
                                            }
                                        }
                                    } else if (lang.equalsIgnoreCase("zh")) {
                                        for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                            if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                                                list.add(promoCodeVOArrayList.get(i).getServiceNameCh());
                                            } else {
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                            if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                                                list.add(promoCodeVOArrayList.get(i).getServiceName());
                                            } else {
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                        if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                                            list.add(promoCodeVOArrayList.get(i).getServiceName());
                                        } else {
                                        }
                                    }
                                }

                                if (list.isEmpty()) {
                                    applyPromoCode.setVisibility(View.GONE);
                                } else {
                                    applyPromoCode.setVisibility(View.VISIBLE);
                                }
                            }

                            adapter = new PaymentAdapter(PaymentActivity.this, cartDatas);
                            paymentRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            if (response.body().getItemTotal() == 0) {
                                viewSubTotal.setVisibility(View.GONE);
                            } else {
                                viewSubTotal.setVisibility(View.VISIBLE);
                                subtotal.setText(NumberFormat.getNumberInstance(Locale.US).format(response.body().getItemTotal()) + " " + getString(R.string.currency));
                            }
                            if (response.body().getDiscountTotalAll() == 0) {
                                viewDiscount.setVisibility(View.GONE);
                            } else if (response.body().getDiscountTotalAll() > 0) {
                                viewDiscount.setVisibility(View.VISIBLE);
                                discount = response.body().getDiscountTotalAll();
                                discountTotal.setText("( " + NumberFormat.getNumberInstance(Locale.US).format(response.body().getDiscountTotalAll()) + " ) " + getString(R.string.currency));
                            }

                            if (response.body().getTotal() == 0) {
                                viewTotal.setVisibility(View.GONE);
                            } else {
                                viewTotal.setVisibility(View.VISIBLE);
                                total.setText(response.body().getTotal() + " " + getString(R.string.currency));
                            }

                        } else {
                            Utility.showToast(getApplicationContext(), response.body().getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<GetCartModel> call, Throwable t) {
                        Utility.showToast(getApplicationContext(), t.getMessage());
                    }
                });

            } else {
                Utility.showToast(getApplicationContext(), getString(R.string.no_internet));

            }
        }
    }

    private void SetMatchPromo(ArrayList<GetCartDataModel> cartData) {
        if (!cartData.isEmpty()) {
            for (int i = 0; i < cartData.size(); i++) {

                ProductPriceListVO products = new ProductPriceListVO();
                products.setPriceId(cartData.get(i).getProductPriceId());
                products.setItemTotal(cartData.get(i).getDiscountTotalPrice());
                productPriceListVOArrayList.add(products);

            }

            matchPromoCodeObject.setProductPriceList(productPriceListVOArrayList);
            matchPromoCodeObject.setPhone(userObj.getPhone());
        }
    }

    private void setUpAdapter() {
        paymentLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        paymentRecyclerView.setLayoutManager(paymentLayoutManager);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

    private void SaveOrder(SaveOrderObject saveOrderObject) {
        progressBar.setVisibility(View.VISIBLE);

        if (Utility.isOnline(getApplicationContext())) {
            serviceProvider.SaveOrderCall(ApiConstants.BASE_URL + ApiConstants.GET_SAVE_ORDER, saveOrderObject).enqueue(new Callback<SaveOrderModel>() {
                @Override
                public void onResponse(Call<SaveOrderModel> call, Response<SaveOrderModel> response) {
                    if (response.body().isError() == false) {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), ThanksActivity.class));

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SaveOrderModel> call, Throwable t) {
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
