package com.busybees.lauk_kaing_expert_services.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.adapters.SaveOrderReview.VendorRatingAdapter;
import com.busybees.lauk_kaing_expert_services.data.models.SaveOrderReview.SaveOrderReviewModel;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyHistory.QuestionsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.VendorInfoVO;
import com.busybees.lauk_kaing_expert_services.data.vos.SaveOrderReview.SaveOrderReviewVO;
import com.busybees.lauk_kaing_expert_services.data.vos.SaveOrderReview.VendorRatingVO;
import com.busybees.lauk_kaing_expert_services.data.vos.Users.UserVO;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.ApiConstants;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener{

    private UserVO userObj = new UserVO();
    private NetworkServiceProvider serviceProvider;

    private ArrayList<QuestionsVO> qlist = new ArrayList<>();
    private ArrayList<VendorRatingVO> datalist=new ArrayList<>();
    private MyHistoryDetailVO historyDataModel;

    private float rate_count;
    private float rate_overall = 0;

    private ImageView cancel;
    private TextView app_rating,arrival_on_time_rating,cust_support_rating,price_rating,service_rating,vendor_rating,overall_rating;
    private EditText comment;
    private Button ok;
    private ProgressBar progressBar;
    private RatingBar app_rating_rate,arrival_on_time_rating_rate,cust_support_rating_rate,price_rating_rate,service_rating_rate,vendor_rating_rate,overall_rating_rate;

    private VendorRatingAdapter vendorAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_rating);

        userObj = Utility.query_UserProfile(this);
        serviceProvider = new NetworkServiceProvider(this);

        app_rating = findViewById(R.id.app_rating);
        cancel = findViewById(R.id.cancel);
        arrival_on_time_rating = findViewById(R.id.arrival_on_time_rating);
        cust_support_rating = findViewById(R.id.cust_support_rating);
        price_rating = findViewById(R.id.price_rating);
        service_rating = findViewById(R.id.service_rating);
        comment = findViewById(R.id.comment);
        ok = findViewById(R.id.btn_rate);
        overall_rating = findViewById(R.id.overall_rating);
        overall_rating_rate = findViewById(R.id.rate_overall_rating);
        app_rating_rate = findViewById(R.id.rate_app_rating);
        arrival_on_time_rating_rate = findViewById(R.id.rate_arrival_on_time_rating);
        cust_support_rating_rate = findViewById(R.id.rate_cust_support_rating);
        price_rating_rate = findViewById(R.id.rate_price_rating);
        service_rating_rate = findViewById(R.id.rate_service_rating);
        progressBar = findViewById(R.id.materialLoader);
        recyclerView = findViewById(R.id.recycle_vendor_rate);

        app_rating_rate.setOnRatingBarChangeListener(this);
        service_rating_rate.setOnRatingBarChangeListener(this);
        price_rating_rate.setOnRatingBarChangeListener(this);
        cust_support_rating_rate.setOnRatingBarChangeListener(this);
        arrival_on_time_rating_rate.setOnRatingBarChangeListener(this);

        if (getIntent() != null) {
            qlist = (ArrayList<QuestionsVO>) getIntent().getSerializableExtra("rate_question");
            rate_count = (float) getIntent().getSerializableExtra("rate_count");
            historyDataModel = (MyHistoryDetailVO) getIntent().getSerializableExtra("history_data");

            service_rating_rate.setRating(rate_count);
            rate_overall = rate_count;

            Log.e("history>>", String.valueOf(historyDataModel.getVendorData()));

            if (historyDataModel != null){
                vendorAdapter= new VendorRatingAdapter(this, historyDataModel.getVendorData());
                recyclerView.setAdapter(vendorAdapter);
                layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(layoutManager);
                vendorAdapter.setClick(this);

            }

            VendorRatingVO vRateObj = new VendorRatingVO();
            for (int i = 0; i < historyDataModel.getVendorData().size() ; i++) {
                vRateObj.setVendorId(historyDataModel.getVendorData().get(i).getVendorId());
                vRateObj.setVendorRating(0);
                datalist.add(vRateObj);
            }

            if (Utility.checkLng(this).equalsIgnoreCase("it") || Utility.checkLng(this).equalsIgnoreCase("fr")){
                if ( MDetect.INSTANCE.isUnicode()){
                    app_rating.setText(qlist.get(1).getAppRating());
                    arrival_on_time_rating.setText(qlist.get(1).getArrivalOnTimeRating());
                    cust_support_rating.setText(qlist.get(1).getCustomerSupportRating());
                    price_rating.setText(qlist.get(1).getPriceRating());
                    service_rating.setText(qlist.get(1).getServiceRating());

                } else  {
                    app_rating.setText(qlist.get(1).getAppRating());
                    arrival_on_time_rating.setText(qlist.get(1).getArrivalOnTimeRating());
                    cust_support_rating.setText(qlist.get(1).getCustomerSupportRating());
                    price_rating.setText(qlist.get(1).getPriceRating());
                    service_rating.setText(qlist.get(1).getServiceRating());
                }

            } else if (Utility.checkLng(this).equalsIgnoreCase("zh")) {
                app_rating.setText(qlist.get(2).getAppRating());
                arrival_on_time_rating.setText(qlist.get(2).getArrivalOnTimeRating());
                cust_support_rating.setText(qlist.get(2).getCustomerSupportRating());
                price_rating.setText(qlist.get(2).getPriceRating());
                service_rating.setText(qlist.get(2).getServiceRating());

            } else{
                app_rating.setText(qlist.get(0).getAppRating());
                arrival_on_time_rating.setText(qlist.get(0).getArrivalOnTimeRating());
                cust_support_rating.setText(qlist.get(0).getCustomerSupportRating());
                price_rating.setText(qlist.get(0).getPriceRating());
                service_rating.setText(qlist.get(0).getServiceRating());

            }


        }

        onClick();
    }

    private void onClick() {
        ok.setOnClickListener(v -> {

            SaveOrderReviewVO soReviewObj = new SaveOrderReviewVO();
            soReviewObj.setUserId(userObj.getId());
            soReviewObj.setAppRating(Math.round(app_rating_rate.getRating()));
            soReviewObj.setArrivalOnTimeRating(Math.round(arrival_on_time_rating_rate.getRating()));
            soReviewObj.setCustSupportRating(Math.round(cust_support_rating_rate.getRating()));
            soReviewObj.setPriceRating(Math.round(price_rating_rate.getRating()));
            soReviewObj.setServiceRating(Math.round(service_rating_rate.getRating()));
            soReviewObj.setVendorRatingArr(datalist);
            soReviewObj.setComment(comment.getText().toString());
            soReviewObj.setOrderDetailId(historyDataModel.getOrderDetailId());
            CallReview(soReviewObj);

        });


        cancel.setOnClickListener(v -> finish());
    }

    private void CallReview(SaveOrderReviewVO obj) {

        if (Utility.isOnline(this)){

            progressBar.setVisibility(View.VISIBLE);
            serviceProvider.SOReview(ApiConstants.BASE_URL + ApiConstants.GET_SAVE_ORDER_REVIEW, obj).enqueue(new Callback<SaveOrderReviewModel>() {

                @Override
                public void onResponse(Call<SaveOrderReviewModel> call, Response<SaveOrderReviewModel> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.body().getError()==false){

                        Utility.showToast(getApplicationContext(), response.body().getMessage());

                        Intent intent = new Intent(RatingActivity.this, HistoryDetailActivity.class);
                        intent.putExtra("key", 1);
                        intent.putExtra("receipt_data", historyDataModel);
                        startActivity(intent);
                        finish();

                    }else {
                        Utility.showToast(getApplicationContext(), response.body().getMessage());
                    }

                }
                @Override
                public void onFailure(Call<SaveOrderReviewModel> call, Throwable t) {
                    Utility.showToast(RatingActivity.this,t.getMessage());
                }
            });

        }else {
            Utility.showToast(this, getString(R.string.no_internet));

        }

    }

    void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void CalculateRate(float rate,int id){

        rate_overall+=rate;

        float overall=rate_overall/6;
        overall_rating_rate.setRating(overall);

    }

    public void RateClick(VendorInfoVO obj, float rate){
        VendorRatingVO vRateObj=new VendorRatingVO();

        for (int i = 0; i <datalist.size() ; i++) {
            if (obj.getVendorId() == datalist.get(i).getVendorId()){
                datalist.remove(i);
            }
        }

        vRateObj.setVendorId(obj.getVendorId());
        vRateObj.setVendorRating(Math.round(rate));
        datalist.add(vRateObj);

        CalculateRate(rate,0);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()){
            case R.id.rate_app_rating:

                CalculateRate(app_rating_rate.getRating(),ratingBar.getId());

                break;


            case R.id.rate_service_rating:

                CalculateRate(service_rating_rate.getRating(),ratingBar.getId());

                break;
            case R.id.rate_price_rating:

                CalculateRate(price_rating_rate.getRating(),ratingBar.getId());

                break;
            case R.id.rate_cust_support_rating:

                CalculateRate(cust_support_rating_rate.getRating(),ratingBar.getId());

                break;
            case R.id.rate_arrival_on_time_rating:

                CalculateRate(arrival_on_time_rating_rate.getRating(),ratingBar.getId());

                break;
        }
    }
}
