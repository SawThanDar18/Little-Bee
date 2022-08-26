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
import com.busybees.lauk_kaing_expert_services.adapters.Receipt.ReceiptDetailAdapter;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.ProductPriceVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class ReceiptDetailActivity extends AppCompatActivity {

    private UserVO userVO;

    private RecyclerView recyclerView;
    private ImageView backButton;
    private TextView c_name,c_phone,c_add;
    private TextView rec_date,cust_id,rec_no,orderid;
    MyHistoryDetailVO receiptModel;
    LinearLayoutManager layoutManager;
    ReceiptDetailAdapter adapter;
    List<ProductPriceVO> productPriceVOList;
    private LinearLayout viewsub, viewdiscount, viewtotal;

    private TextView subtotal, distotal, total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarVisible();
        setContentView(R.layout.receipt_detail_activity);

        userVO = Utility.query_UserProfile(this);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.reycle_payment);
        c_name = (TextView) findViewById(R.id.cust_name);
        c_phone = (TextView) findViewById(R.id.cust_phone);
        c_add = (TextView) findViewById(R.id.cust_add);
        rec_date = (TextView) findViewById(R.id.rec_date);
        cust_id = (TextView) findViewById(R.id.cust_id);
        rec_no=(TextView)findViewById(R.id.rec_rno);
        orderid=(TextView)findViewById(R.id.order_id);
        subtotal = (TextView) findViewById(R.id.sub_total_amount);
        distotal = (TextView) findViewById(R.id.discount_total_amount);
        total = (TextView) findViewById(R.id.total_amount);
        viewsub=(LinearLayout)findViewById(R.id.sub_total_view);
        viewdiscount=(LinearLayout)findViewById(R.id.discount_view);
        viewtotal=(LinearLayout)findViewById(R.id.total_view);

        if (getIntent() != null) {
            receiptModel= (MyHistoryDetailVO) getIntent().getSerializableExtra("history_data");
            if (receiptModel != null && userVO != null){
                rec_date.setText(receiptModel.getCompleteDate());
                c_name.setText(userVO.getUsername());
                c_phone.setText(receiptModel.getPhone());
                c_add.setText(receiptModel.getOrderAddress());
                cust_id.setText(String.valueOf(userVO.getId()));
                orderid.setText(String.valueOf(receiptModel.getOrderDetailId()));
                //rec_no.setText(String.valueOf(receiptModel.getRecNo()));

            /*if (receiptModel.getTotalPrice() == 0) {
                viewSubTotal.setVisibility(View.GONE);
            } else {
                viewSubTotal.setVisibility(View.VISIBLE);
                subtotal.setText(receiptModel.getTotalPrice() + " " + getString(R.string.currency));
            }

             if (receiptModel.getDiscountTotal()==0){
                distotal.setText("-");
            }else if (receiptModel.getDiscountTotal()>0){
                distotal.setText("( "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(receiptModel.getDiscountTotal()))+" )"+"  MMK");
            }

            if (myHistoryDetailVO.getPromoTotalDiscount() == 0) {
                viewPromoCode.setVisibility(View.GONE);
            } else {
                viewPromoCode.setVisibility(View.VISIBLE);
                promo_discount.setText("( " + NumberFormat.getNumberInstance(Locale.US).format(myHistoryDetailVO.getPromoTotalDiscount()) + " ) " + getString(R.string.currency));
            }*/

                if (receiptModel.getTotalPrice() == 0) {
                    viewtotal.setVisibility(View.GONE);
                } else {
                    viewtotal.setVisibility(View.VISIBLE);
                    total.setText(receiptModel.getTotalPrice() + " " + getString(R.string.currency));
                }

                productPriceVOList =receiptModel.getProductPrice();

                adapter= new ReceiptDetailAdapter(this, productPriceVOList);
                recyclerView.setAdapter(adapter);
            }
        }

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.reycle_payment);
        c_name = (TextView) findViewById(R.id.cust_name);
        c_phone = (TextView) findViewById(R.id.cust_phone);
        c_add = (TextView) findViewById(R.id.cust_add);
        rec_date = (TextView) findViewById(R.id.rec_date);
        cust_id = (TextView) findViewById(R.id.cust_id);
        rec_no=(TextView)findViewById(R.id.rec_rno);
        orderid=(TextView)findViewById(R.id.order_id);
        subtotal = (TextView) findViewById(R.id.sub_total_amount);
        distotal = (TextView) findViewById(R.id.discount_total_amount);
        total = (TextView) findViewById(R.id.total_amount);
        viewsub=(LinearLayout)findViewById(R.id.sub_total_view);
        viewdiscount=(LinearLayout)findViewById(R.id.discount_view);
        viewtotal=(LinearLayout)findViewById(R.id.total_view);

        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}


