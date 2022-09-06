package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.LeadFormImageAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyOrdersDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.VendorInfoAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.PhotoItemViewAdapter;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.MyOrdersDetailVO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderId, orderAddress, orderTime, orderDate;
    private RecyclerView orderDetailRecyclerView, leadFormPhotosRecyclerView, leadFormOnePhotosRecyclerView, vendorInfoRecyclerView;

    private LinearLayout viewSubTotal, viewDiscount, viewTotal, viewPromoCode, leadFormView, leadFormOneView, vendorInfoLayouot;
    private TextView subtotal, discountTotal, total, promo_discount;

    private TextView confirmPrice, title, budget, squareFeet, details;

    private ImageView back;

    private MyOrdersDetailAdapter myOrdersDetailAdapter;
    private VendorInfoAdapter vendorInfoAdapter;
    private LinearLayoutManager layoutManager, leadFormLayoutManager;

    private MyOrdersDetailVO myOrdersDetailVO = new MyOrdersDetailVO();

    private LeadFormImageAdapter leadFormImageAdapter;
    List<String> photos = new ArrayList<>();
    List<String> image_URLs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_order_detail);

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
        vendorInfoLayouot = findViewById(R.id.vendor_layout);
        vendorInfoRecyclerView = findViewById(R.id.vendor_info_recyclerview);

        if (getIntent() != null) {
            myOrdersDetailVO = (MyOrdersDetailVO) getIntent().getSerializableExtra("order_detail");

            orderId.setText("Order No : "+ myOrdersDetailVO.getOrderDetailId());
            orderAddress.setText(myOrdersDetailVO.getOrderAddress());
            orderDate.setText(myOrdersDetailVO.getDate());
            orderTime.setText(myOrdersDetailVO.getTime());

            if (myOrdersDetailVO.getOriginalTotal() == 0) {
                viewSubTotal.setVisibility(View.GONE);
            } else {
                viewSubTotal.setVisibility(View.VISIBLE);
                subtotal.setText(getString(R.string.currency) + " " + myOrdersDetailVO.getOriginalTotal());
            }

            if (myOrdersDetailVO.getTotalDiscount() == 0 && myOrdersDetailVO.getPromoTotalDiscount() == 0) {
                viewSubTotal.setVisibility(View.GONE);
            } else {
                viewSubTotal.setVisibility(View.VISIBLE);
            }

            if (myOrdersDetailVO.getTotalDiscount() == 0) {
                viewDiscount.setVisibility(View.GONE);
            } else {
                viewDiscount.setVisibility(View.VISIBLE);
                discountTotal.setText("( " + getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(myOrdersDetailVO.getTotalDiscount()) + " )");
            }

            if (myOrdersDetailVO.getPromoTotalDiscount() == 0) {
                viewPromoCode.setVisibility(View.GONE);
            } else {
                viewPromoCode.setVisibility(View.VISIBLE);
                promo_discount.setText("( " + getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(myOrdersDetailVO.getPromoTotalDiscount()) + " )");
            }

            if (myOrdersDetailVO.getTotalPrice() == 0) {
                viewTotal.setVisibility(View.GONE);
            } else {
                viewTotal.setVisibility(View.VISIBLE);
                total.setText(getString(R.string.currency) + " " + myOrdersDetailVO.getTotalPrice());
            }

            if (myOrdersDetailVO.getGeneralFormInfo() != null) {
                if (myOrdersDetailVO.getGeneralFormInfo().getFormStatus() == 2) {
                    leadFormView.setVisibility(View.VISIBLE);
                    leadFormOneView.setVisibility(View.GONE);
                    showLeadFormDetail();
                } else if (myOrdersDetailVO.getGeneralFormInfo().getFormStatus() == 1){
                    leadFormView.setVisibility(View.GONE);
                    leadFormOneView.setVisibility(View.VISIBLE);
                    showLeadFormOneDetail();
                }
            } else {
                leadFormView.setVisibility(View.GONE);
                leadFormOneView.setVisibility(View.GONE);
            }

            if (myOrdersDetailVO.getStatus().equalsIgnoreCase("Ongoing") || myOrdersDetailVO.getStatus().equalsIgnoreCase("Upcoming") || myOrdersDetailVO.getStatus().equalsIgnoreCase("Confirmed")){
                vendorInfoLayouot.setVisibility(View.VISIBLE);
            }else {
                vendorInfoLayouot.setVisibility(View.GONE);
            }

        }

        back.setOnClickListener(v-> finish());

        setUpAdapter();
    }

    private void showLeadFormOneDetail() {
        for (int i = 0; i < myOrdersDetailVO.getGeneralFormInfo().getImages().size(); i++) {
            photos.add(myOrdersDetailVO.getGeneralFormInfo().getImages().get(i));
            image_URLs.add(myOrdersDetailVO.getGeneralFormInfo().getImagesLink() + photos.get(i));
        }

        leadFormImageAdapter = new LeadFormImageAdapter(getApplicationContext());
        leadFormOnePhotosRecyclerView.setAdapter(leadFormImageAdapter);
        leadFormImageAdapter.setData(image_URLs);
        leadFormImageAdapter.notifyDataSetChanged();
    }

    private void showLeadFormDetail() {
        title.setText(myOrdersDetailVO.getGeneralFormInfo().getTitle());
        budget.setText(String.valueOf(myOrdersDetailVO.getGeneralFormInfo().getBudget()));
        squareFeet.setText(myOrdersDetailVO.getGeneralFormInfo().getSquareFeet());
        details.setText(myOrdersDetailVO.getGeneralFormInfo().getDetailText());

        for (int i = 0; i < myOrdersDetailVO.getGeneralFormInfo().getImages().size(); i++) {
            photos.add(myOrdersDetailVO.getGeneralFormInfo().getImages().get(i));
            image_URLs.add(myOrdersDetailVO.getGeneralFormInfo().getImagesLink() + photos.get(i));
        }

        leadFormImageAdapter = new LeadFormImageAdapter(getApplicationContext());
        leadFormPhotosRecyclerView.setAdapter(leadFormImageAdapter);
        leadFormImageAdapter.setData(image_URLs);
        leadFormImageAdapter.notifyDataSetChanged();
    }

    private void setUpAdapter() {
        myOrdersDetailAdapter = new MyOrdersDetailAdapter(this, myOrdersDetailVO.getProductPrice());
        orderDetailRecyclerView.setAdapter(myOrdersDetailAdapter);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderDetailRecyclerView.setLayoutManager(layoutManager);

        leadFormLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        leadFormPhotosRecyclerView.setLayoutManager(leadFormLayoutManager);

        leadFormLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        leadFormOnePhotosRecyclerView.setLayoutManager(leadFormLayoutManager);

        vendorInfoAdapter = new VendorInfoAdapter(this, myOrdersDetailVO.getVendorData(), myOrdersDetailVO.getStatus());
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
