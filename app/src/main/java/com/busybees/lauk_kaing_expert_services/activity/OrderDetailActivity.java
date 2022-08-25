package com.busybees.lauk_kaing_expert_services.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.Orders.MyOrdersDetailAdapter;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.MyOrdersDetailVO;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView orderId, orderAddress, orderTime, orderDate;
    private RecyclerView orderDetailRecyclerView;

    private MyOrdersDetailAdapter myOrdersDetailAdapter;
    private LinearLayoutManager layoutManager;

    private MyOrdersDetailVO myOrdersDetailVO = new MyOrdersDetailVO();

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

        if (getIntent() != null) {
            myOrdersDetailVO = (MyOrdersDetailVO) getIntent().getSerializableExtra("order_detail");

            orderId.setText("Order No : "+ myOrdersDetailVO.getOrderDetailId());
            orderAddress.setText(myOrdersDetailVO.getOrderAddress());
            orderDate.setText(myOrdersDetailVO.getDate());
            orderTime.setText(myOrdersDetailVO.getTime());

        }

        setUpAdapter();
    }

    private void setUpAdapter() {
        myOrdersDetailAdapter = new MyOrdersDetailAdapter(this, myOrdersDetailVO.getProductPrice());
        orderDetailRecyclerView.setAdapter(myOrdersDetailAdapter);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderDetailRecyclerView.setLayoutManager(layoutManager);
    }

    void makeStatusBarVisible() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
