package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.Dialog.DialogRating;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.LeadFormImageAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyHistoryDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyOrdersDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.VendorInfoAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Receipt.HistoryVendorInfoAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.ReOrder.ReOrderModel;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ReOrder.ReOrderProductPriceVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ReOrder.ReOrderVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDetailActivity extends AppCompatActivity {

    private UserVO userVO;
    private NetworkServiceProvider networkServiceProvider;

    private TextView orderId, orderAddress, orderTime, orderDate, viewReceipt, rate;
    private RecyclerView orderDetailRecyclerView, leadFormPhotosRecyclerView, leadFormOnePhotosRecyclerView, vendorInfoRecyclerView;

    private LinearLayout viewSubTotal, viewDiscount, viewTotal, viewPromoCode, leadFormView, leadFormOneView, vendorInfoLayout;
    private TextView subtotal, discountTotal, total, promo_discount;

    private TextView confirmPrice, title, budget, squareFeet, details, reOrder;

    private ImageView back;

    private MyHistoryDetailAdapter myHistoryDetailAdapter;
    private HistoryVendorInfoAdapter vendorInfoAdapter;
    private LinearLayoutManager layoutManager, leadFormLayoutManager;

    private MyHistoryDetailVO myHistoryDetailVO = new MyHistoryDetailVO();
    private ArrayList<QuestionsVO> questionsVOArrayList = new ArrayList<>();

    private LeadFormImageAdapter leadFormImageAdapter;
    List<String> photos = new ArrayList<>();
    List<String> image_URLs = new ArrayList<>();

    private int key = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_history_detail);

        userVO = Utility.query_UserProfile(this);
        networkServiceProvider = new NetworkServiceProvider(this);

        orderId = findViewById(R.id.order_no);
        orderAddress = findViewById(R.id.order_address);
        orderDetailRecyclerView = findViewById(R.id.order_detail_recyclerview);
        orderDate = findViewById(R.id.order_date);
        orderTime = findViewById(R.id.order_time);
        viewSubTotal = findViewById(R.id.sub_total_view);
        viewDiscount = findViewById(R.id.discount_view);
        viewTotal = findViewById(R.id.total_view);
        viewPromoCode = findViewById(R.id.promo_discount_view);
        subtotal = findViewById(R.id.sub_total_amount);
        discountTotal = findViewById(R.id.discount_total_amount);
        total = findViewById(R.id.total_amount);
        promo_discount = findViewById(R.id.promo_amount);
        leadFormView = findViewById(R.id.lead_form_view);
        confirmPrice = findViewById(R.id.confirm_price);
        title = findViewById(R.id.title);
        budget = findViewById(R.id.budget);
        squareFeet = findViewById(R.id.sqfeet);
        details = findViewById(R.id.details);
        leadFormPhotosRecyclerView = findViewById(R.id.photos_recyclerview);
        back = findViewById(R.id.back_button);
        leadFormOneView = findViewById(R.id.lead_form_one_view);
        leadFormOnePhotosRecyclerView = findViewById(R.id.lead_form_one_photos_recyclerview);
        viewReceipt = findViewById(R.id.view_receipt);
        vendorInfoLayout = findViewById(R.id.vendor_layout);
        vendorInfoRecyclerView = findViewById(R.id.vendor_info_recyclerview);
        reOrder = findViewById(R.id.re_order_btn);
        rate = findViewById(R.id.rate);

        if (getIntent() != null) {
            myHistoryDetailVO = (MyHistoryDetailVO) getIntent().getSerializableExtra("receipt_data");
            questionsVOArrayList = (ArrayList<QuestionsVO>) getIntent().getSerializableExtra("question");

            orderId.setText("Order No : "+ myHistoryDetailVO.getOrderDetailId());
            orderAddress.setText(myHistoryDetailVO.getOrderAddress());
            orderDate.setText(myHistoryDetailVO.getDate());
            orderTime.setText(myHistoryDetailVO.getTime());

            if (myHistoryDetailVO.getOriginalTotal() == 0) {
                viewSubTotal.setVisibility(View.GONE);
            } else {
                viewSubTotal.setVisibility(View.VISIBLE);
                subtotal.setText(getString(R.string.currency) + " " + myHistoryDetailVO.getOriginalTotal());
            }

            if (myHistoryDetailVO.getTotalDiscount() == 0 && myHistoryDetailVO.getPromoTotalDiscount() == 0) {
                viewSubTotal.setVisibility(View.GONE);
            } else {
                viewSubTotal.setVisibility(View.VISIBLE);
            }

            if (myHistoryDetailVO.getTotalDiscount() == 0) {
                viewDiscount.setVisibility(View.GONE);
            } else {
                viewDiscount.setVisibility(View.VISIBLE);
                discountTotal.setText("( " + getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(myHistoryDetailVO.getTotalDiscount()) + " )");
            }

            if (myHistoryDetailVO.getPromoTotalDiscount() == 0) {
                viewPromoCode.setVisibility(View.GONE);
            } else {
                viewPromoCode.setVisibility(View.VISIBLE);
                promo_discount.setText("( " + getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(myHistoryDetailVO.getPromoTotalDiscount()) + " )");
            }

            if (myHistoryDetailVO.getTotalPrice() == 0) {
                viewTotal.setVisibility(View.GONE);
            } else {
                viewTotal.setVisibility(View.VISIBLE);
                total.setText(getString(R.string.currency) + " " + myHistoryDetailVO.getTotalPrice());
            }

            if (myHistoryDetailVO.getGeneralFormInfo().getId() == 0 || myHistoryDetailVO.getGeneralFormInfo() == null) {

                reOrder.setVisibility(View.VISIBLE);
                leadFormView.setVisibility(View.GONE);
                leadFormOneView.setVisibility(View.GONE);

            } else {
                reOrder.setVisibility(View.GONE);
                if (myHistoryDetailVO.getGeneralFormInfo().getFormStatus() == 2) {
                    leadFormView.setVisibility(View.VISIBLE);
                    leadFormOneView.setVisibility(View.GONE);
                    showLeadFormDetail();
                } else if (myHistoryDetailVO.getGeneralFormInfo().getFormStatus() == 1){
                    leadFormView.setVisibility(View.GONE);
                    leadFormOneView.setVisibility(View.VISIBLE);
                    showLeadFormOneDetail();
                }
            }

            if (myHistoryDetailVO.getOverAllRating() == 0) {
                rate.setVisibility(View.VISIBLE);
                rate.setOnClickListener(v -> {
                    if (myHistoryDetailVO.getOverAllRating()==0){
                        DialogRating dialogRating=new DialogRating(questionsVOArrayList, myHistoryDetailVO);
                        dialogRating.show(getSupportFragmentManager(),"");
                    }else {
                        Utility.showToast(HistoryDetailActivity.this,getString(R.string.all_ready_rated));
                    }

                });
            } else {
                rate.setVisibility(View.GONE);
            }

            key = getIntent().getIntExtra("key", 0);
            if (key == 1) {
                rate.setVisibility(View.GONE);
            }

        }


        onClick();
        setUpAdapter();
    }

    private void onClick() {
        back.setOnClickListener(v-> finish());

        viewReceipt.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryDetailActivity.this, ReceiptDetailActivity.class);
            intent.putExtra("history_data", myHistoryDetailVO);
            startActivity(intent);
        });

        reOrder.setOnClickListener(v -> {
            ReOrderVO reOrderVO = new ReOrderVO();

            ReOrderProductPriceVO reOrderProductPriceVO = new ReOrderProductPriceVO();
            for (int i = 0; i < myHistoryDetailVO.getProductPrice().size() ; i++) {

                reOrderProductPriceVO.setQuantity(String.valueOf(myHistoryDetailVO.getProductPrice().get(0).getQuantity()));
                reOrderProductPriceVO.setProduct_price_id(String.valueOf(myHistoryDetailVO.getProductPrice().get(0).getProductPriceId()));
            }

            Log.e("ProductPriceId>>", reOrderProductPriceVO.getProduct_price_id());

            reOrderVO.setProductPriceObject(reOrderProductPriceVO);
            reOrderVO.setPhone(userVO.getPhone());
            CallReOrder(reOrderVO);
        });
    }

    private void CallReOrder(ReOrderVO obj) {

        if (Utility.isOnline(this)){

            networkServiceProvider.ReOrder(ApiConstants.BASE_URL + ApiConstants.GET_ADD_TO_CART_REORDER, obj).enqueue(new Callback<ReOrderModel>() {

                @Override
                public void onResponse(Call<ReOrderModel> call, Response<ReOrderModel> response) {
                    if (response.body().getError()==false){

                        Utility.showToast(HistoryDetailActivity.this,response.body().getMessage());
                        startActivity(new Intent(HistoryDetailActivity.this, AddressActivity.class));
                        finish();
                    }else {

                        Utility.showToast(HistoryDetailActivity.this,response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<ReOrderModel> call, Throwable t) {

                    Utility.showToast(HistoryDetailActivity.this, t.getMessage());

                }
            });

        }else {

            Utility.showToast(this, getString( R.string.no_internet));

        }

    }

    private void showLeadFormOneDetail() {
        for (int i = 0; i < myHistoryDetailVO.getGeneralFormInfo().getImages().size(); i++) {
            photos.add(myHistoryDetailVO.getGeneralFormInfo().getImages().get(i));
            image_URLs.add(myHistoryDetailVO.getGeneralFormInfo().getImagesLink() + photos.get(i));
        }

        leadFormImageAdapter = new LeadFormImageAdapter(getApplicationContext());
        leadFormOnePhotosRecyclerView.setAdapter(leadFormImageAdapter);
        leadFormImageAdapter.setData(image_URLs);
        leadFormImageAdapter.notifyDataSetChanged();
    }

    private void showLeadFormDetail() {
        title.setText(myHistoryDetailVO.getGeneralFormInfo().getTitle());
        budget.setText(String.valueOf(myHistoryDetailVO.getGeneralFormInfo().getBudget()));
        squareFeet.setText(myHistoryDetailVO.getGeneralFormInfo().getSquareFeet());
        details.setText(myHistoryDetailVO.getGeneralFormInfo().getDetailText());

        for (int i = 0; i < myHistoryDetailVO.getGeneralFormInfo().getImages().size(); i++) {
            photos.add(myHistoryDetailVO.getGeneralFormInfo().getImages().get(i));
            image_URLs.add(myHistoryDetailVO.getGeneralFormInfo().getImagesLink() + photos.get(i));
        }

        leadFormImageAdapter = new LeadFormImageAdapter(getApplicationContext());
        leadFormPhotosRecyclerView.setAdapter(leadFormImageAdapter);
        leadFormImageAdapter.setData(image_URLs);
        leadFormImageAdapter.notifyDataSetChanged();
    }

    private void setUpAdapter() {
        myHistoryDetailAdapter = new MyHistoryDetailAdapter(this, myHistoryDetailVO.getProductPrice());
        orderDetailRecyclerView.setAdapter(myHistoryDetailAdapter);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderDetailRecyclerView.setLayoutManager(layoutManager);

        leadFormLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        leadFormPhotosRecyclerView.setLayoutManager(leadFormLayoutManager);

        leadFormLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        leadFormOnePhotosRecyclerView.setLayoutManager(leadFormLayoutManager);

        vendorInfoAdapter = new HistoryVendorInfoAdapter(this, myHistoryDetailVO.getVendorData());
        vendorInfoRecyclerView.setAdapter(vendorInfoAdapter);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        vendorInfoRecyclerView.setLayoutManager(layoutManager);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
