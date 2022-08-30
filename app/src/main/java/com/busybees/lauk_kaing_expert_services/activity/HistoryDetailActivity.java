package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
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
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyHistoryDetailAdapter;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyOrdersDetailAdapter;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsVO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryDetailActivity extends AppCompatActivity {

    private TextView orderId, orderAddress, orderTime, orderDate, viewReceipt;
    private RecyclerView orderDetailRecyclerView, leadFormPhotosRecyclerView, leadFormOnePhotosRecyclerView;

    private LinearLayout viewSubTotal, viewDiscount, viewTotal, viewPromoCode, leadFormView, leadFormOneView;
    private TextView subtotal, discountTotal, total, promo_discount;

    private TextView confirmPrice, title, budget, squareFeet, details;

    private ImageView back;

    private MyHistoryDetailAdapter myHistoryDetailAdapter;
    private LinearLayoutManager layoutManager, leadFormLayoutManager;

    private MyHistoryDetailVO myHistoryDetailVO = new MyHistoryDetailVO();
    private ArrayList<QuestionsVO> questionsVOArrayList = new ArrayList<>();

    private LeadFormImageAdapter leadFormImageAdapter;
    List<String> photos = new ArrayList<>();
    List<String> image_URLs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.activity_history_detail);

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

            if (myHistoryDetailVO.getGeneralFormInfo() != null) {
                if (myHistoryDetailVO.getGeneralFormInfo().getFormStatus() == 2) {
                    leadFormView.setVisibility(View.VISIBLE);
                    leadFormOneView.setVisibility(View.GONE);
                    showLeadFormDetail();
                } else if (myHistoryDetailVO.getGeneralFormInfo().getFormStatus() == 1){
                    leadFormView.setVisibility(View.GONE);
                    leadFormOneView.setVisibility(View.VISIBLE);
                    showLeadFormOneDetail();
                }
            } else {
                leadFormView.setVisibility(View.GONE);
                leadFormOneView.setVisibility(View.GONE);
            }

            /*if (receiptModel.getOverallRating() == 0) {
                rate.setVisibility(View.VISIBLE);
                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (receiptModel.getOverallRating()==0){
                            DialogRating dialogRating=new DialogRating(qlist,receiptModel);
                            dialogRating.show(getSupportFragmentManager(),"");
                        }else {
                            //rate.setVisibility(View.GONE);
                            Utility.showToast(HistoryDetailActivity.this,getString(R.string.all_ready_rated));
                        }

                    }
                });
            } else {
                rate.setVisibility(View.GONE);
            }*/

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
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
