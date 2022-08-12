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
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.MatchPromoCodeObject;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.ProductPriceListVO;
import com.busybees.lauk_kaing_expert_services.data.vos.PromoCOde.PromoCodeVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    private LinearLayout continueLayout, viewSubTotal, viewDiscount, viewTotal;

    private ProgressBar progressBar;
    private TextView subtotal, discountTotal, total;

    private ImageView back, homePageView, cartPageView, addressPageView, finalOrderPageView;

    ArrayList<GetCartDataModel> cartDatas = new ArrayList<>();
    ArrayList<PromoCodeVO> promoCodeVOArrayList = new ArrayList<>();

    private String serviceName;

    private ArrayList<ProductPriceListVO> productPriceListVOArrayList = new ArrayList<>();
    private ProductPriceListVO productPriceListVO = new ProductPriceListVO();
    private MatchPromoCodeObject matchPromoCodeObject = new MatchPromoCodeObject();

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

        setUpAdapter();
        onClick();
        CallGetCart();
    }

    private void onClick() {
        continueLayout.setOnClickListener(v -> {
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
                String lang = AppStorePreferences.getString(getApplicationContext(), AppENUM.LANG);
                ArrayList<String> list = new ArrayList<String>();

                for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                    if (promoCodeVOArrayList.get(i).getPromoActive() == 1) {
                        list.add(promoCodeVOArrayList.get(i).getServiceName());
                    }
                }

                TextView promoCodeName = promoCodeDialogView.findViewById(R.id.promo_code_name);

                /*if (lang != null) {
                    if (lang.equalsIgnoreCase("it") || lang.equalsIgnoreCase("fr")) {
                        if (MDetect.INSTANCE.isUnicode()) {
                            for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                list.add(promoCodeVOArrayList.get(i).getServiceName());
                            }
                        } else {
                            for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                                list.add(promoCodeVOArrayList.get(i).getServiceName());
                            }
                        }
                    } else if (lang.equalsIgnoreCase("zh")) {
                        for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                            list.add(promoCodeVOArrayList.get(i).getServiceName());
                        }
                    } else {
                        for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                            list.add(promoCodeVOArrayList.get(i).getServiceName());
                        }
                    }
                } else {
                    for (int i = 0; i < promoCodeVOArrayList.size(); i++) {
                        list.add(promoCodeVOArrayList.get(i).getServiceName());
                    }
                }*/

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentActivity.this, android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                services_spinner.setAdapter(arrayAdapter);

                services_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        serviceName = promoCodeVOArrayList.get(position).getServiceName();
                        promoCodeName.setText(promoCodeVOArrayList.get(position).getPromoCode());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                promoCodeDialogView.findViewById(R.id.promo_code_ok_btn).setOnClickListener(v1 -> {

                    if (!promoCodeName.getText().toString().equalsIgnoreCase("")) {
                        for (int carts = 0; carts < cartDatas.size(); carts++) {
                            productPriceListVO.setPriceId(cartDatas.get(carts).getProductPriceId());
                            productPriceListVO.setItemTotal(cartDatas.get(carts).getAmount());
                            productPriceListVOArrayList.add(productPriceListVO);
                        }

                        matchPromoCodeObject.setProductPriceList(productPriceListVOArrayList);
                        matchPromoCodeObject.setPhone(userObj.getPhone());
                        matchPromoCodeObject.setPromoCode(promoCodeName.getText().toString());
                        CallMatchPromoCode(matchPromoCodeObject);

                    } else {

                    }
                });

                promoCodeDialogView.findViewById(R.id.promo_cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promoCodeDialog.dismiss();
                        /*specificInfo.setPrice_id("");
                        specificInfo.setVcode("");
                        sameCode();*/
                    }
                });

                promoCodeDialog.show();

            }

        });

        back.setOnClickListener(v -> finish());


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

    private void CallMatchPromoCode(MatchPromoCodeObject matchPromoCodeObject) {
        if (Utility.isOnline(getApplicationContext())) {
            serviceProvider.MatchPromoCode(ApiConstants.BASE_URL + ApiConstants.GET_MATCH_PROMO_CODE, matchPromoCodeObject).enqueue(new Callback<MatchPromoCodeModel>() {
                @Override
                public void onResponse(Call<MatchPromoCodeModel> call, Response<MatchPromoCodeModel> response) {
                    if (response.body().isError() == false) {
                        Utility.showToast(getApplicationContext(), response.body().getMessage());

                    } else if (response.body().isError() == true) {
                        Utility.showToast(PaymentActivity.this, response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<MatchPromoCodeModel> call, Throwable t) {
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
                            promoCodeVOArrayList.addAll(response.body().getPromoCodeList());
                            adapter = new PaymentAdapter(PaymentActivity.this, cartDatas);
                            paymentRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            subtotal.setText(NumberFormat.getNumberInstance(Locale.US).format(response.body().getItemTotal()) + getString(R.string.currency));
                            if (response.body().getDiscountTotalAll() == 0) {
                                discountTotal.setText("-");
                            } else if (response.body().getDiscountTotalAll() > 0) {
                                discountTotal.setText("( " + NumberFormat.getNumberInstance(Locale.US).format(response.body().getDiscountTotalAll()) + " )" + getString(R.string.currency));
                            }

                            total.setText(response.body().getTotal() + getString(R.string.currency));

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

    private void setUpAdapter() {
        paymentLayoutManager = new LinearLayoutManager(PaymentActivity.this, LinearLayoutManager.VERTICAL, false);
        paymentRecyclerView.setLayoutManager(paymentLayoutManager);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
}
